package com.fluidminds.android.studiosity.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fluidminds.android.studiosity.BR;
import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.models.BackupModel;
import com.fluidminds.android.studiosity.utils.Converters;
import com.fluidminds.android.studiosity.viewmodels.BackupViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.query.SortOrder;
import com.google.android.gms.drive.query.SortableField;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

/**
 * A fragment to Backup or Restore app data.
 */
public class BackupFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private ViewDataBinding mBinding;
    private BackupViewModel mViewModel;
    private LinearLayout mLastBackupInfo;
    private Button mButtonBackupNow;
    private GoogleApiClient mGoogleApiClient;
    private eConnectionReason mConnectionReason = eConnectionReason.FRAG_INITIAL_ONRESUME;
    private static final String sSTUDIOSITY = "Studiosity";
    private ProgressDialog mProgressDialog;

    // used by async callbacks to determine the reason for the connection and how to proceed
    private enum eConnectionReason {
        NONE,
        FRAG_INITIAL_ONRESUME,
        ACCOUNT_SELECTION_DIALOG_CANCELED,
        NOCONNECTED_BACKUP_NOW
    }

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    private static final String TAG = "BackupFragment";

    public BackupFragment() {
        // Required empty public constructor
    }

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This fragment really doesn't have a fragment menu but it's an
        // opportunity to color the toolbar after the Activity menu is loaded
        setHasOptionsMenu(true);

        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_backup, container, false);

        // mViewModel not null on orientation change
        if (mViewModel == null) {
            mViewModel = new BackupViewModel(new BackupModel());
        }
        else {
            // close the dialog if it's open on orientation change
            mConnectionReason = eConnectionReason.ACCOUNT_SELECTION_DIALOG_CANCELED;
        }

        mBinding.setVariable(BR.viewModel, mViewModel);

        // Inflate the layout for this fragment
        View view = mBinding.getRoot();

        mLastBackupInfo = (LinearLayout) view.findViewById(R.id.lastBackupInfo);

        // color button background using primary color
        ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xff9C27B0});
        mButtonBackupNow = (Button) view.findViewById(R.id.buttonBackupNow);
        ((AppCompatButton)mButtonBackupNow).setSupportBackgroundTintList(csl);
        mButtonBackupNow.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.buttonBackupNow:
                if (!mGoogleApiClient.isConnected()) {
                    mConnectionReason = eConnectionReason.NOCONNECTED_BACKUP_NOW;
                    mGoogleApiClient.connect();
                }
                else {
                    uploadBackup();
                }
                break;
        }
    }

    /**
     * Called when fragment gets visible.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Drive.API)
                    .addApi(Plus.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    /**
     * Called when fragment is no longer visible.
     */
    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Handles resolution callbacks.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION) {
            if (resultCode == RESULT_CANCELED && data == null)
                mConnectionReason = eConnectionReason.ACCOUNT_SELECTION_DIALOG_CANCELED;
            else if (resultCode == RESULT_OK)
                mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mConnectionReason == eConnectionReason.NOCONNECTED_BACKUP_NOW) {
            mConnectionReason = eConnectionReason.NONE;
            uploadBackup();
        }
        else {
            mConnectionReason = eConnectionReason.NONE;

            // Perform fetch off the UI thread.
            new GetLastBackupMetadataAsyncTask().execute();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution is
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());

        mButtonBackupNow.setVisibility(View.VISIBLE);

        // don't show Account selection dialog
        if (mConnectionReason == eConnectionReason.ACCOUNT_SELECTION_DIALOG_CANCELED || mConnectionReason == eConnectionReason.FRAG_INITIAL_ONRESUME) {
            mConnectionReason = eConnectionReason.NONE;
            return;
        }

        if (!connectionResult.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), connectionResult.getErrorCode(), 0).show();
            return;
        }
        try {
            connectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /*
     * Create a backup of the Studiosity database and save it in the Cloud. If the user switches
     * devices they can restore the database to the new device.
     */
    private void uploadBackup() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMax(100);
            mProgressDialog.setTitle("Backing Up Data");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }

        // temporarily disable orientation change during upload
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        // Perform upload off the UI thread.
        new UploadBackupAsyncTask().execute();
    }

    /**
     * Check if root/Studiosity folder exists; if not, create it
     */
    private DriveId getStudiosityFolderDriveId(boolean createIfNotExist) {
        DriveId studiosityFolder = null;
        Query query = new Query.Builder()
                .addFilter(Filters.and(Filters.eq(
                                SearchableField.TITLE, sSTUDIOSITY),
                        Filters.eq(SearchableField.TRASHED, false)))
                .build();

        DriveApi.MetadataBufferResult result = Drive.DriveApi.getRootFolder(mGoogleApiClient).queryChildren(mGoogleApiClient, query)
                .await();

        if (!result.getStatus().isSuccess()) {
            showMessage(getResources().getString(R.string.cloud_error));
        } else {
            for (Metadata m : result.getMetadataBuffer()) {
                if (m.getTitle().equals(sSTUDIOSITY)) {
                    studiosityFolder = m.getDriveId();
                    break;
                }
            }

            if (studiosityFolder == null && createIfNotExist) {
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle(sSTUDIOSITY)
                        .build();

                DriveFolder.DriveFolderResult createFolderResult = Drive.DriveApi.getRootFolder(mGoogleApiClient)
                        .createFolder(mGoogleApiClient, changeSet).await();

                if (!createFolderResult.getStatus().isSuccess()) {
                    showMessage(getResources().getString(R.string.cloud_error));
                }
                else {
                    studiosityFolder = createFolderResult.getDriveFolder().getDriveId();
                }
            }
        }

        return studiosityFolder;
    }

    /**
     * Back up database to a file.
     */
    private DriveContents createBackupContent(DriveId studiosityFolder) {

        DriveApi.DriveContentsResult contentsResult = Drive.DriveApi.newDriveContents(mGoogleApiClient).await();
        if (!contentsResult.getStatus().isSuccess()) {
            showMessage(getResources().getString(R.string.cloud_error));
            return null;
        }

        // write content to DriveContents
        OutputStream outputStream = contentsResult.getDriveContents().getOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);
        try {
            writer.write("Hello World! This is a test of Google Drive - in the future this will be a backup of the database!");
            writer.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return contentsResult.getDriveContents();
    }

    /**
     * Upload the backup file to Google Drive
     */
    private boolean createBackupDriveFile(DriveId studiosityFolder, DriveContents driveContents) {

        String fileName = String.format(getString(R.string.backup_file_name), Converters.dateToString(new Date(), "yyyy-MM-dd-HH-mm-ss"));
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(fileName)
                //.setMimeType("application/json")
                .setMimeType("text/plain")
                .setStarred(true).build();

        // create a file in the Studiosity folder
        DriveFolder.DriveFileResult createFileResult = studiosityFolder.asDriveFolder().createFile(mGoogleApiClient, changeSet, driveContents).await();

        if (!createFileResult.getStatus().isSuccess()) {
            showMessage(getResources().getString(R.string.cloud_error));
            return false;
        }
        else
            return true;
    }

    /**
     * Shows a toast message.
     */
    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Get the metadata of the most current backup file (if it exists)
     */
    private class GetLastBackupMetadataAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DriveId studiosityFolder = getStudiosityFolderDriveId(false);

            SortOrder sortOrder = new SortOrder.Builder()
                    .addSortDescending(SortableField.MODIFIED_DATE).build();

            Query query = new Query.Builder()
                    .addFilter(Filters.and(Filters.contains(SearchableField.TITLE, "StudiosityBackup_"),
                            Filters.eq(SearchableField.TRASHED, false)))
                    .setSortOrder(sortOrder)
                    .build();

            DriveApi.MetadataBufferResult metadataResult = studiosityFolder.asDriveFolder().queryChildren(mGoogleApiClient, query).await();

            if (metadataResult.getStatus().isSuccess()) {
                for (Metadata m : metadataResult.getMetadataBuffer()) {
                    if (m.getTitle().startsWith("StudiosityBackup_")) {
                        mViewModel.getModel().setModifiedDate(m.getModifiedDate());
                        mViewModel.getModel().setFileSize(m.getFileSize());
                        mViewModel.getModel().setAccount(Plus.AccountApi.getAccountName(mGoogleApiClient));
                        return  true;
                    }
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                mLastBackupInfo.setVisibility(View.VISIBLE);

            mButtonBackupNow.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Get the metadata of the most current backup file (if it exists)
     */
    private class UploadBackupAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            DriveId studiosityFolder = getStudiosityFolderDriveId(true);
            if (studiosityFolder == null)
                mProgressDialog.dismiss();

            DriveContents driveContents = createBackupContent(studiosityFolder);
            if (driveContents == null)
                mProgressDialog.dismiss();

//                    while (progressDoalog.getProgress() <= progressDoalog
//                            .getMax()) {
//                        Thread.sleep(200);
//                        handle.sendMessage(handle.obtainMessage());
//                        if (progressDoalog.getProgress() == progressDoalog
//                                .getMax()) {
//                            progressDoalog.dismiss();
//                        }
//                    }

            return createBackupDriveFile(studiosityFolder, driveContents);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                new GetLastBackupMetadataAsyncTask().execute();
            else
                mButtonBackupNow.setVisibility(View.VISIBLE);

            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

            mProgressDialog.dismiss();
        }
    }
}
