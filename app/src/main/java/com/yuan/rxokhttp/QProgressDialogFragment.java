//package com.yuan.library;
//
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//
//
///**
// * Created by shucheng.qu on 2017/10/24.
// */
//
//public class QProgressDialogFragment extends DialogFragment {
//
//    private OnCancelListener mCancelListener;
//    private String progressMessage;
//    private TextView tvMessage;
//    private ProgressBar loading;
//    private Handler handler;
//
//    public static QProgressDialogFragment newInstance(String progressMessage, String progressSuccess, String progressFailed, boolean cancelable, OnCancelListener cancelListener) {
//        QProgressDialogFragment frag = new QProgressDialogFragment();
//        frag.setCancelable(cancelable);
//        frag.setCancelListener(cancelListener);
//        Bundle args = new Bundle();
//        args.putString("progressMessage", progressMessage);
//        frag.setArguments(args);
//        return frag;
//    }
//
//    public static QProgressDialogFragment newInstance(String progressMessage, String progressSuccess, boolean cancelable, OnCancelListener cancelListener) {
//        return newInstance(progressMessage, progressSuccess, "", cancelable, cancelListener);
//    }
//
//    public static QProgressDialogFragment newInstance(String progressMessage, boolean cancelable, OnCancelListener cancelListener) {
//        return newInstance(progressMessage, "", cancelable, cancelListener);
//    }
//
//
//    @Override
//    public int getTheme() {
//        return R.style.progress_fragmeng;
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Bundle myBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
//        if (myBundle == null) {
//            myBundle = new Bundle();
//        }
//        handler = new Handler();
//        progressMessage = myBundle.getString("progressMessage", "");
//        Dialog onCreateDialog = super.onCreateDialog(savedInstanceState);
//        onCreateDialog.setContentView(R.layout.pub_progressdialog_layout);
//        tvMessage = (TextView) onCreateDialog.findViewById(android.R.id.message);
//        loading = (ProgressBar) onCreateDialog.findViewById(R.id.pb_loading);
//        ViewUtils.setOrGone(tvMessage, progressMessage);
//        tvMessage.setText(progressMessage);
//        onCreateDialog.setCanceledOnTouchOutside(false);
//        return onCreateDialog;
//    }
//
//    @Override
//    public void show(FragmentManager manager, String tag) {
//        try {
//            super.show(manager, tag);
//        } catch (IllegalStateException ise) {
//            try {
//                FragmentTransaction ft = manager.beginTransaction();
//                ft.add(this, tag);
//                ft.commitAllowingStateLoss();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void dismiss(boolean success, String msg) {
//        if (success) {
//            ViewUtils.setOrGone(tvMessage, msg);
//            ViewUtils.setOrGone(loading, false);
////            ViewUtils.setOrGone(ivFailed, false);
////            ViewUtils.setOrGone(ivSuccess, true);
//        } else {
//            ViewUtils.setOrGone(tvMessage, msg);
//            ViewUtils.setOrGone(loading, false);
////            ViewUtils.setOrGone(ivFailed, true);
////            ViewUtils.setOrGone(ivSuccess, false);
//        }
//        handler.postDelayed(this::fff, 500);
//    }
//
//    private void fff() {
//        super.dismissAllowingStateLoss();
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismissAllowingStateLoss();
//    }
//
//    @Override
//    public void onCancel(DialogInterface dialog) {
//        OnCancelListener cancelListener = getCancelListener();
//        if (cancelListener != null) {
//            cancelListener.onCancel(dialog);
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("progressMessage", progressMessage);
//    }
//
//    public void setCancelListener(OnCancelListener cancelListener) {
//        this.mCancelListener = cancelListener;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        handler.removeCallbacksAndMessages(null);
//    }
//
//    public OnCancelListener getCancelListener() {
//        return mCancelListener;
//    }
//}
