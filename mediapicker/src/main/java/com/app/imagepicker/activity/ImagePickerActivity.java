package com.app.imagepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.imagepicker.GridInsetDecoration;
import com.app.imagepicker.R;
import com.app.imagepicker.adapter.ImageFolderAdapter;
import com.app.imagepicker.adapter.ImageItemAdapter;
import com.app.imagepicker.base.activity.BaseActivity;
import com.app.imagepicker.beans.ImageFolder;
import com.app.imagepicker.beans.ImageItem;
import com.app.imagepicker.constants.PickMode;
import com.app.imagepicker.controller.LoadImage;
import com.app.imagepicker.controller.LoadImageListener;
import com.app.imagepicker.listeners.OnImageItemListener;
import com.app.imagepicker.utils.CollectionUtil;
import com.app.imagepicker.utils.FileUtil;
import com.app.imagepicker.utils.ScreenUtil;
import com.app.imagepicker.utils.TakePhotoHelper;
import com.app.imagepicker.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.app.imagepicker.activity.ImageCropActivity.RESULT_PATH;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImagePickerActivity extends BaseActivity implements LoadImageListener {

    private static final String TAG = "ImagePickerActivity";

    public static final String EXTRA_PICK_MODE = TAG + ".extra.pick.mode";
    public static final String EXTRA_MAX_COUNT = TAG + ".extra.pick.maxCount";
    public static final String EXTRA_SELECTED = TAG + ".extra.pick.selected";
    public static final String EXTRA_ROW_COUNT = TAG + ".extra.pick.rowCount";
    public static final String EXTRA_SHOW_CAMERA = TAG + ".extra.pick.showCamera";
    public static final String EXTRA_WIDTH = TAG + ".extra.pick.width";
    public static final String EXTRA_HEIGHT = TAG + ".extra.pick.height";
    public static final String EXTRA_CUSTOM_PICK_TEXT_HINT_RES = TAG + ".extra.pick.textHint";
    public static final String EXTRA_AVATAR_PATH = TAG + ".extra.pick.avatar.path";

    private int mMaxCount;
    private int mPickMode;
    private int mRowCount;
    //裁剪图片时宽高
    private int mWidth = 0;
    private int mHeight = 0;
    private boolean mShowCamera = false;
    private String mAvatarFilePath;

    private @StringRes
    int mPickTextHintRes;

    public static final String EXTRA_FILE_CHOOSE_INTERCEPTOR = "EXTRA_FILE_CHOOSE_INTERCEPTOR";

    public static final int REQUEST_CODE_PICKER_PREVIEW = 100;
    public static final int REQUEST_CODE_CROP_IMAGE = 101;

    private RecyclerView mRecyclerView;
    private ImageItemAdapter mImageItemAdapter;

    //private ImageView mNavIcon;
    //private TextView mTitle;
    /**
     * general_ok
     */
    private TextView mSureTv;

    private ImageFolder mCurrFolder;
    /**
     * 全部图片
     */
    private Button mBtnDir;
    private ListPopupWindow mFolderPopupWindow;
    private View mFooterView;
    private List<ImageFolder> mFolders;
    private List<ImageItem> mImageItems = new ArrayList<>();
    private ImageFolderAdapter mImageFolderAdapter;

    private ArrayList<ImageItem> mSelectedItems;
    private TextView mSelectedSize;
    private LoadImage mLoadImage;

    public static Intent getIntent(int maxCount, int pickMode, int rowCount,
        ArrayList<ImageItem> selected, boolean showCamera, int width, int height,
        @StringRes int pickRes, String avatarFilePath) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MAX_COUNT, maxCount);
        intent.putExtra(EXTRA_PICK_MODE, pickMode);
        intent.putParcelableArrayListExtra(EXTRA_SELECTED, selected);
        intent.putExtra(EXTRA_ROW_COUNT, rowCount);
        intent.putExtra(EXTRA_SHOW_CAMERA, showCamera);
        intent.putExtra(EXTRA_WIDTH, width);
        intent.putExtra(EXTRA_HEIGHT, height);
        intent.putExtra(EXTRA_CUSTOM_PICK_TEXT_HINT_RES, pickRes);
        //        intent.putExtra(EXTRA_FILE_CHOOSE_INTERCEPTOR, fileChooseInterceptor);
        intent.putExtra(EXTRA_AVATAR_PATH, avatarFilePath);
        return intent;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadImage = new LoadImage(this, this);

        mLoadImage.loadImageFolders();

        mLoadImage.loadAllImages();
    }

    @Override protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mSureTv = findViewById(R.id.sureTv);
        mSelectedSize = findViewById(R.id.selected_image_size);
        mFooterView = findViewById(R.id.footer_panel);
        mBtnDir = findViewById(R.id.btn_dir);

        mTitleBar.setActivity(this);
        mTitleBar.setMiddleTitle(R.string.general_all_pictures);
        mTitleBar.setBackBtnIcon(R.drawable.default_back);
        updateSize(mSelectedItems);
        GridLayoutManager layoutManager = new GridLayoutManager(this, mRowCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
            new GridInsetDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));

        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResultAction();
                finish();
            }
        });
        mBtnDir.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                backgroundAlpha(0.3f);
                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                }
            }
        });
    }

    private void updateSize(ArrayList<ImageItem> items) {
        int count = CollectionUtil.isEmpty(items) ? 0 : items.size();
        if (count > 0) {
            mSelectedSize.setVisibility(View.VISIBLE);
            mSelectedSize.setText(String.valueOf(count));
        } else {
            mSelectedSize.setVisibility(View.GONE);
        }
    }

    private void setResultAction() {
        ArrayList<ImageItem> items = new ArrayList<>();
        if (mImageItemAdapter != null) {
            items = mImageItemAdapter.getSelectedItems();
        }

        Intent bundle = new Intent();
        bundle.putParcelableArrayListExtra(EXTRA_SELECTED, items);
        setResult(RESULT_OK, bundle);
    }

    @Override public int getLayoutResourceId() {
        return R.layout.activity_image_picker;
    }

    @Override protected void handleIntent(Intent intent) {
        mMaxCount = intent.getIntExtra(EXTRA_MAX_COUNT, 1);
        mPickMode = intent.getIntExtra(EXTRA_PICK_MODE, PickMode.MODE_IMAGE);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        mRowCount = intent.getIntExtra(EXTRA_ROW_COUNT, 4);
        mWidth = intent.getIntExtra(EXTRA_WIDTH, 0);
        mHeight = intent.getIntExtra(EXTRA_HEIGHT, 0);
        if (mWidth <= 0 || mHeight <= 0) {
            mWidth = ScreenUtil.getInstance().getScreenWidth(getContext());
            mHeight = ScreenUtil.getInstance().getScreenWidth(getContext());
        }
        mAvatarFilePath = intent.getStringExtra(EXTRA_AVATAR_PATH);
        mPickTextHintRes =
            intent.getIntExtra(EXTRA_CUSTOM_PICK_TEXT_HINT_RES, R.string.general_send);

        mSelectedItems = intent.getParcelableArrayListExtra(EXTRA_SELECTED);
    }

    @Override public void loadFolders(List<ImageFolder> folders) {
        mFolders = folders;
    }

    @Override public void loadImages(final List<ImageItem> items) {
        mImageItems.clear();
        mImageItems = items;
        if (mImageItemAdapter == null) {
            initImageItemAdapter(items);
        } else {
            mImageItemAdapter.updateItems(items);
        }
    }

    private void initImageItemAdapter(final List<ImageItem> items) {
        mImageItemAdapter =
            new ImageItemAdapter(mRecyclerView, items, mMaxCount, mShowCamera, mPickMode);
        final TakePhotoHelper mHelper = new TakePhotoHelper(this);
        mImageItemAdapter.setSelectItems(mSelectedItems);
        mImageItemAdapter.setImageItemListener(new OnImageItemListener() {
            @Override public void onSelected(ImageItem item) {
                updateSize(mImageItemAdapter.getSelectedItems());
            }

            @Override public void onUnSelected(ImageItem item) {
                updateSize(mImageItemAdapter.getSelectedItems());
            }

            @Override public void onMaxChecked(int max) {
                ToastUtil.getInstance()
                    .show(ImagePickerActivity.this,
                        getResources().getString(R.string.string_beyong_max, max));
            }

            @Override public void onPreview(ImageItem item, int pos, boolean checked,
                ArrayList<ImageItem> mSelectedItems) {

                if (mPickMode == PickMode.MODE_IMAGE) {
                    ArrayList<ImageItem> list = new ArrayList<>();
                    list.addAll(mImageItems);
                    ImagePreviewActivity.start(ImagePickerActivity.this, list, mSelectedItems,
                        mMaxCount, pos, checked, REQUEST_CODE_PICKER_PREVIEW);
                } else {
                    ImageCropActivity.start(ImagePickerActivity.this, item.path, mAvatarFilePath,
                        REQUEST_CODE_CROP_IMAGE, mWidth, mHeight);
                }
            }

            @Override public void onTakePhoto() {
                mHelper.takePhoto();
            }
        });
        mRecyclerView.setAdapter(mImageItemAdapter);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICKER_PREVIEW) {
            if (data != null) {
                ArrayList<ImageItem> selected =
                    data.getParcelableArrayListExtra(ImagePreviewActivity.EXTRA_SELECTED_IMAGES);
                mImageItemAdapter.setSelectItems(selected);

                updateSize(selected);

                boolean finish = data.getBooleanExtra(ImagePreviewActivity.EXTRA_FINISH, false);

                if (finish) {
                    setResultAction();
                    finish();
                }
            }
        } else if (REQUEST_CODE_CROP_IMAGE == requestCode) {
            if (data != null) {

                String destFile = data.getStringExtra(RESULT_PATH);

                Intent bundle = new Intent();
                bundle.putExtra(RESULT_PATH, destFile);
                setResult(RESULT_OK, bundle);
                finish();
            }
        } else if (TakePhotoHelper.CAPTURE_PHOTO_REQUEST_CODE == requestCode) {
            if (FileUtil.getFileSize(TakePhotoHelper.photoFile.getAbsolutePath()) > 0) {
                ImageCropActivity.start(ImagePickerActivity.this,
                    TakePhotoHelper.photoFile.getAbsolutePath(), mAvatarFilePath,
                    REQUEST_CODE_CROP_IMAGE, mWidth, mHeight);
            }
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new ListPopupWindow(this);
        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;
        //mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mImageFolderAdapter = new ImageFolderAdapter(this, mFolders, 0);
        mFolderPopupWindow.setAdapter(mImageFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(mFooterView);
        mFolderPopupWindow.setModal(true);

        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        mFolderPopupWindow.setAnimationStyle(R.style.popupwindow_anim_style);

        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mFolderPopupWindow.dismiss();

                ImageFolder folder = mFolders.get(position);
                mCurrFolder = folder;

                mImageFolderAdapter.setCurrPos(position);

                mTitleBar.setMiddleTitle(mCurrFolder.name);
                mBtnDir.setText(mCurrFolder.name);

                mLoadImage.reloadImages(folder);
            }
        });
    }

    // 设置屏幕透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        getWindow().setAttributes(lp);
    }
}
