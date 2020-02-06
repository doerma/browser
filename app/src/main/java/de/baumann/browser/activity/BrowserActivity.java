package de.baumann.browser.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.zxing.BarcodeFormat;
import com.mobapphome.mahencryptorlib.MAHEncryptor;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import de.baumann.browser.browser.AdBlock;
import de.baumann.browser.browser.AlbumController;
import de.baumann.browser.browser.BrowserContainer;
import de.baumann.browser.browser.BrowserController;
import de.baumann.browser.browser.Cookie;
import de.baumann.browser.browser.Javascript;
import de.baumann.browser.database.BookmarkList;
import de.baumann.browser.database.Record;
import de.baumann.browser.database.RecordAction;
import de.baumann.browser.Ninja.R;
import de.baumann.browser.service.ClearService;
import de.baumann.browser.task.ScreenshotTask;
import de.baumann.browser.unit.BrowserUnit;
import de.baumann.browser.unit.HelperUnit;
import de.baumann.browser.unit.IntentUnit;
import de.baumann.browser.unit.ViewUnit;
import de.baumann.browser.view.CompleteAdapter;
import de.baumann.browser.view.GridAdapter;
import de.baumann.browser.view.GridAdapter_filter;
import de.baumann.browser.view.GridItem;

import de.baumann.browser.view.GridItem_filter;
import de.baumann.browser.view.NinjaToast;
import de.baumann.browser.view.NinjaWebView;
import de.baumann.browser.view.Adapter_Record;
import de.baumann.browser.view.SwipeTouchListener;

import static android.content.ContentValues.TAG;

@SuppressWarnings({"FieldCanBeLocal", "ApplySharedPref"})
public class BrowserActivity extends AppCompatActivity implements BrowserController, View.OnClickListener {

    // Menus

    private TextView menu_newTabOpen;
    private TextView menu_closeTab;
    private TextView popupmenu_quitW;
    //private TextView popupmenu_quit;

    private TextView menu_shareScreenshot;
    private TextView menu_shareLink;
    private TextView menu_sharePDF;
    private TextView menu_openWith;

    private TextView menu_download;
    private TextView menu_saveScreenshot;
    private TextView menu_saveBookmark;
    private TextView menu_savePDF;
    private TextView menu_saveStart;
    private TextView menu_fileManager;

    private TextView menu_homepage;
    private TextView menu_shortcut;
    private TextView menu_openFavorite;
    private TextView menu_openBookmark;
    private TextView menu_openHistory;
    private TextView menu_shareCP;

    private ImageButton tab_plus;
    private ImageButton tab_close;

    private Adapter_Record adapter;

    // main menunormal and submenu
    private PopupWindow popupMainMenu;
    private PopupWindow popupMainMenuW;
    private PopupWindow popupSubmenuMore;
    private PopupWindow popupSubmenuSave;
    private PopupWindow popupSubmenuShare;
    //popupmenuw
    private TextView popupmenu_normalW;
    private AppCompatImageButton popupmenu_backW;
    private AppCompatImageButton popupmenu_forwardW;
    private AppCompatImageButton popupmenu_search_websiteW;
    private AppCompatImageButton popupmenu_overvieww;
    private AppCompatImageButton popupmenu_addBookmarkw;
    //popupmenu&popupmenuw
    private LinearLayout popups;
    private LinearLayout popupsW;
    private TextView popupmenu_share;
    private TextView popupmenu_save;
    private TextView popupmenu_settings;
    private TextView popupmenu_other;
    private TextView popupmenu_shareW;
    private TextView popupmenu_saveW;
    private TextView popupmenu_settingsW;
    private TextView popupmenu_otherW;
    //popupmenuw
    private TextView popupmenu_wintan;
    private AppCompatImageButton popupmenu_overview;
    private AppCompatImageButton popupmenu_homepage;
    private AppCompatImageButton popupmenu_addBookmark;
    //private AppCompatImageButton popupmenu_showFavorite;
    private AppCompatImageButton popupmenu_search_website;
    private AppCompatImageButton popupmenu_refresh;

    // Views
    protected TextView titleTv;
    protected TextView urlTv;
    protected TextView TitleTv;

    protected AppCompatImageButton close;
    protected AppCompatImageButton back;
    protected AppCompatImageButton forward;
    protected AppCompatImageButton more;
    protected AppCompatImageButton search_go;
    protected AppCompatImageButton search_chooseEngine;
    protected AppCompatImageButton qrcode_scan;
    protected AppCompatImageButton history_refresh;
    protected AppCompatImageButton bookmark_home;

    private ImageButton searchUp;
    private ImageButton searchDown;
    private ImageButton searchCancel;
    //private ImageButton omniboxRefresh;
    //private ImageButton omniboxMainmenu;
    //private ImageButton omniboxOverview;

    private ImageButton open_favorite;
    private ImageButton open_bookmark;
    private ImageButton open_history;
    private ImageButton open_menu;

    private FloatingActionButton fab_imageButtonNav;
    private AutoCompleteTextView inputBox;
    private ProgressBar progressBar;
    private EditText searchBox;
    private TextView howMatch;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialog_OverView;
    private BottomSheetDialog bottomSheetDialog_Records;
    private NinjaWebView ninjaWebView;
    private ListView listView;
    //private TextView omniboxTitle;
    private TextView dialogTitle;
    private GridView gridView;
    private View customView;
    private VideoView videoView;

    private HorizontalScrollView tab_ScrollView;

    // Layouts

    private RelativeLayout appBar;
    private RelativeLayout toolbar;
    protected RelativeLayout urlbar;
    private RelativeLayout searchPanel;

    private FrameLayout contentFrame;
    private LinearLayout tab_container;
    private FrameLayout fullscreenHolder;

    private View open_favoriteView;
    private View open_bookmarkView;
    private View open_historyView;

    private LinearLayout centerLayout;
    private RelativeLayout  normalCenterLayout;

    // Others
    private String title;
    private String url;
    private String CollectionTab;
    private BroadcastReceiver downloadReceiver;
    private BottomSheetBehavior mBehavior;
    private int intFontzoom;

    private boolean rtl;
    private boolean isUIWintan;
    private boolean isLoading;

    private boolean showIcon = true;
    private boolean disableIcon = false;

    private Activity activity;
    private Context context;
    private SharedPreferences sp;
    private MAHEncryptor mahEncryptor;
    private Javascript javaHosts;
    private Cookie cookieHosts;
    private AdBlock adBlock;
    private GridAdapter gridAdapter;


    private boolean prepareRecord() {
        NinjaWebView webView = (NinjaWebView) currentAlbumController;
        String title = webView.getTitle();
        String url = webView.getUrl();
        return (title == null
                || title.isEmpty()
                || url == null
                || url.isEmpty()
                || url.startsWith(BrowserUnit.URL_SCHEME_ABOUT)
                || url.startsWith(BrowserUnit.URL_SCHEME_MAIL_TO)
                || url.startsWith(BrowserUnit.URL_SCHEME_INTENT));
    }

    private int originalOrientation;
    private float dimen156dp;
    private float dimen117dp;

    private boolean searchOnSite;
    private boolean onPause;

    private WebChromeClient.CustomViewCallback customViewCallback;
    private ValueCallback<Uri[]> filePathCallback = null;
    private AlbumController currentAlbumController = null;

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int REQUEST_QR_CODE = 2;

    private ValueCallback<Uri[]> mFilePathCallback;

    // Classes

    private class VideoCompletionListener implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
        @Override
        public void onCompletion(MediaPlayer mp) {
            onHideCustomView();
        }
    }

    // Overrides

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        WebView.enableSlowWholeDocumentDraw();
        //StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //StrictMode.setVmPolicy(builder.build());

        context = BrowserActivity.this;
        activity = BrowserActivity.this;

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("restart_changed", 0).apply();
        sp.edit().putBoolean("pdf_create", false).commit();
        rtl = sp.getBoolean("SP_RTL_9",false);
        isUIWintan=sp.getBoolean("SP_WINTAN_9",false);
        isLoading = false;

        HelperUnit.applyTheme(context);
        setContentView(R.layout.activity_main);

        if (Objects.requireNonNull(sp.getString("saved_key_ok", "no")).equals("no")) {
            char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!§$%&/()=?;:_-.,+#*<>".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 25; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            if (Locale.getDefault().getCountry().equals("CN")) {
                sp.edit().putString(getString(R.string.sp_search_engine), "2").apply();
            }
            sp.edit().putString("saved_key", sb.toString()).apply();
            sp.edit().putString("saved_key_ok", "yes").apply();

            sp.edit().putString("setting_gesture_tb_up", "08").apply();
            sp.edit().putString("setting_gesture_tb_down", "01").apply();
            sp.edit().putString("setting_gesture_tb_left", "07").apply();
            sp.edit().putString("setting_gesture_tb_right", "06").apply();

            sp.edit().putString("setting_gesture_nav_up", "04").apply();
            sp.edit().putString("setting_gesture_nav_down", "05").apply();
            sp.edit().putString("setting_gesture_nav_left", "03").apply();
            sp.edit().putString("setting_gesture_nav_right", "02").apply();

            sp.edit().putBoolean(getString(R.string.sp_location), false).apply();
        }

        try {
            mahEncryptor = MAHEncryptor.newInstance(Objects.requireNonNull(sp.getString("saved_key", "")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        contentFrame = findViewById(R.id.main_content);
        appBar = findViewById(R.id.appBar);

        dimen156dp = getResources().getDimensionPixelSize(R.dimen.layout_width_156dp);
        dimen117dp = getResources().getDimensionPixelSize(R.dimen.layout_height_117dp);

        initOmnibox();
        initSearchPanel();
        initOverview();
        initCollections();
        initMenu();


        new AdBlock(context); // For AdBlock cold boot
        new Javascript(context);
        new Cookie(context);

        downloadReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                bottomSheetDialog = new BottomSheetDialog(context);
                View dialogView = View.inflate(context, R.layout.dialog_action, null);
                TextView textView = dialogView.findViewById(R.id.dialog_text);
                textView.setText(R.string.toast_downloadComplete);
                Button action_ok = dialogView.findViewById(R.id.action_ok);
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                        hideBottomSheetDialog ();
                    }
                });
                Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                action_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideBottomSheetDialog ();
                    }
                });
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
        dispatchIntent(getIntent());

        if(isUIWintan) {
            setWintanUI();
        }else {
            setNormalUI();
        }

        if (sp.getBoolean("start_tabStart", false)){
            showCollections();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, 250);
        }

    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        //todo
        // 获取二维码扫码结果
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            final String result = data.getStringExtra("result");
            boolean isNum = result.matches("[0-9]+");
            if (isNum == false) {
                //如果是网址，则访问
                updateAlbum(result);
                hideUrlPanel();
            } else {
                //如果是数字（条形码）
                NinjaToast.show(BrowserActivity.this, result);
            }

            return;
        }else if (requestCode == INPUT_FILE_REQUEST_CODE  && mFilePathCallback != null) {
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    // If there is not data, then we may have taken a photo
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onPause() {
        onPause = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sp.getInt("restart_changed", 1) == 1) {
            sp.edit().putInt("restart_changed", 0).apply();
            final BottomSheetDialog dialog = new BottomSheetDialog(context);
            View dialogView = View.inflate(context, R.layout.dialog_action, null);
            TextView textView = dialogView.findViewById(R.id.dialog_text);
            textView.setText(R.string.toast_restart);
            Button action_ok = dialogView.findViewById(R.id.action_ok);
            action_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDestroy();
                }
            });
            Button action_cancel = dialogView.findViewById(R.id.action_cancel);
            action_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.setContentView(dialogView);
            dialog.show();
            HelperUnit.setBottomSheetBehavior(dialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
        }

        dispatchIntent(getIntent());
        updateOmnibox();

        if (sp.getBoolean("pdf_create", false)) {
            sp.edit().putBoolean("pdf_create", false).commit();

            if (sp.getBoolean("pdf_share", false)) {
                sp.edit().putBoolean("pdf_share", false).commit();
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            } else {
                bottomSheetDialog = new BottomSheetDialog(context);
                View dialogView = View.inflate(context, R.layout.dialog_action, null);
                TextView textView = dialogView.findViewById(R.id.dialog_text);
                textView.setText(R.string.toast_downloadComplete);

                Button action_ok = dialogView.findViewById(R.id.action_ok);
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                        hideBottomSheetDialog ();
                    }
                });
                Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                action_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideBottomSheetDialog ();
                    }
                });
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
            }
        }

        if(sp.getBoolean("flag_secure",false)){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    @Override
    public void onDestroy() {
        if (sp.getBoolean(getString(R.string.sp_clear_quit), false)) {
            Intent toClearService = new Intent(this, ClearService.class);
            startService(toClearService);
        }
        BrowserContainer.clear();
        IntentUnit.setContext(null);
        unregisterReceiver(downloadReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            
            case KeyEvent.KEYCODE_MENU:
                if(more.getVisibility()==View.VISIBLE) {
                    return showMainMenu(more);
                }
                else if(fab_imageButtonNav.getVisibility()==View.VISIBLE) {
                    return showMainMenu(more);
                }
            case KeyEvent.KEYCODE_BACK:
                // TODO: 2020-02-02
                hideKeyboard(activity);
                hideOverview();
                hideCollections();
                if (fullscreenHolder != null || customView != null || videoView != null) {
                    return onHideCustomView();
                }else {
                    goback();
                }
                return true;
        }
        return false;
    }

    @Override
    public synchronized void showAlbum(AlbumController controller) {

        if (currentAlbumController != null) {
            currentAlbumController.deactivate();
            View av = (View) controller;
            contentFrame.removeAllViews();
            contentFrame.addView(av);
        } else {
            contentFrame.removeAllViews();
            contentFrame.addView((View) controller);
        }

        currentAlbumController = controller;
        currentAlbumController.activate();
        updateOmnibox();
    }

    @Override
    public void updateAutoComplete() {
        RecordAction action = new RecordAction(this);
        action.open(false);
        List<Record> list = action.listEntries(activity, true);
        action.close();
        CompleteAdapter adapter = new CompleteAdapter(this, R.layout.complete_item, list);
        inputBox.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        inputBox.setThreshold(1);
        inputBox.setDropDownVerticalOffset(-16);
        inputBox.setDropDownWidth(ViewUnit.getWindowWidth(this));
        inputBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((TextView) view.findViewById(R.id.complete_item_url)).getText().toString();
                updateAlbum(url);
                hideKeyboard(activity);
            }
        });
    }

    private void showOverview() {

        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        if (currentAlbumController != null) {
            currentAlbumController.deactivate();
            currentAlbumController.activate();
        }

        if (currentAlbumController != null) {
            currentAlbumController.deactivate();
            currentAlbumController.activate();
        }

        bottomSheetDialog_OverView.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tab_ScrollView.smoothScrollTo(currentAlbumController.getAlbumView().getLeft(), 0);
            }
        }, 250);
    }
    private void showCollections() {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetDialog_Records.show();
    }
    public void hideOverview () {
        if (bottomSheetDialog_OverView != null) {
            bottomSheetDialog_OverView.cancel();
        }
    }
    public void hideCollections() {
        if (bottomSheetDialog_Records != null) {
            bottomSheetDialog_Records.cancel();
        }
    }

    private void hideBottomSheetDialog() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.cancel();
        }
    }

    @Override
    public void onClick(View v) {

        RecordAction action = new RecordAction(context);
        ninjaWebView = (NinjaWebView) currentAlbumController;
        Intent settings;
        boolean isWintan = sp.getBoolean(getString(R.string.sp_wintan_mode),false);

        try {
            title = ninjaWebView.getTitle().trim();
            url = ninjaWebView.getUrl().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (v.getId()) {


            case R.id.popupmenu_wintan:
                setWintanUI();
                popupMainMenu.dismiss();
                NinjaToast.show(context, getString(R.string.toast_Wintan));
                break;
            case R.id.popupmenu_normalw:
                setNormalUI();
                popupMainMenuW.dismiss();
                NinjaToast.show(context, getString(R.string.toast_Normal));
                break;

            case R.id.popupmenu_backw:
                popupMainMenuW.dismiss();
            case R.id.back:
                goback();
                break;

            case R.id.popupmenu_forwardw:
                popupMainMenuW.dismiss();
            case R.id.forward:
                if (ninjaWebView.canGoForward()) {
                    ninjaWebView.goForward();
                } else {
                    NinjaToast.show(context, R.string.toast_webview_forward);
                }
                break;
            // Menu overflow


            case R.id.submenu_newTabOpen:
                popupSubmenuMore.dismiss();
            case R.id.tab_plus:
                hideBottomSheetDialog();
                hideOverview();
                addAlbum(getString(R.string.app_name), sp.getString("favoriteURL", "https://github.com/scoute-dich/browser"), true);
                if (sp.getBoolean("start_tabStart", false)) {
                    showCollections();
                    if (!CollectionTab.equals(getString(R.string.album_title_favorite)))
                        open_favorite.performClick();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }, 250);
                }
                break;
            case R.id.submenu_closeTab:
                popupSubmenuMore.dismiss();
                removeAlbum(currentAlbumController);
                break;


            case R.id.popupmenu_homepage:
                popupMainMenu.dismiss();
            case R.id.action_home:
                if (sp.getBoolean("start_tabStart", false)) {
                    showCollections();
                    if (!CollectionTab.equals(getString(R.string.album_title_favorite)))
                        open_favorite.performClick();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }, 250);
                } else {
                    updateAlbum(sp.getString("favoriteURL", "https://github.com/scoute-dich/browser"));
                }
                break;

            case R.id.submenu_openFavorite:
                popupSubmenuMore.dismiss();
                showCollections();
                if (!CollectionTab.equals(getString(R.string.album_title_favorite)))
                    open_favorite.performClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 250);
                break;

            case R.id.submenu_openBookmark:
                popupSubmenuMore.dismiss();
                showCollections();
                if (!CollectionTab.equals(getString(R.string.album_title_bookmarks)))
                    open_bookmark.performClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 250);
                break;

            case R.id.submenu_openHistory:
                popupSubmenuMore.dismiss();
                showCollections();
                if(!CollectionTab.equals(getString(R.string.album_title_history)))
                    open_history.performClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 250);
                break;


            case R.id.popupmenu_quit:
                if(isWintan)
                    popupMainMenuW.dismiss();
                else
                    popupMainMenu.dismiss();
            case R.id.close:
                doubleTapsQuit();
                break;

            case R.id.submenu_shareScreenshot:
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    int hasWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                        HelperUnit.grantPermissionsStorage(activity);
                    } else {
                        popupSubmenuShare.dismiss();
                        sp.edit().putInt("screenshot", 1).apply();
                        new ScreenshotTask(context, ninjaWebView).execute();
                    }
                } else {
                    popupSubmenuShare.dismiss();
                    sp.edit().putInt("screenshot", 1).apply();
                    new ScreenshotTask(context, ninjaWebView).execute();
                }
                break;

            case R.id.submenu_shareLink:
                popupSubmenuShare.dismiss();
                if (prepareRecord()) {
                    NinjaToast.show(context,getString(R.string.toast_share_failed));
                } else {
                    IntentUnit.share(context, title, url);
                }
                break;

            case R.id.submenu_sharePDF:
                popupSubmenuShare.dismiss();
                printPDF(true);
                break;

            case R.id.submenu_openWith:
                popupSubmenuShare.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                Intent chooser = Intent.createChooser(intent, getString(R.string.menu_open_with));
                startActivity(chooser);
                break;

            case R.id.submenu_saveScreenshot:
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    int hasWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                        HelperUnit.grantPermissionsStorage(activity);
                    } else {
                        popupSubmenuSave.dismiss();
                        sp.edit().putInt("screenshot", 0).apply();
                        new ScreenshotTask(context, ninjaWebView).execute();
                    }
                } else {
                    popupSubmenuSave.dismiss();
                    sp.edit().putInt("screenshot", 0).apply();
                    new ScreenshotTask(context, ninjaWebView).execute();
                }
                break;

            case R.id.submenu_saveBookmark:
                popupSubmenuSave.dismiss();
            case R.id.popupmenu_addBookmark:
            case R.id.popupmenu_addBookmarkw:
                if(isWintan)
                    popupMainMenuW.dismiss();
                else
                    popupMainMenu.dismiss();
                try {
                    MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(Objects.requireNonNull(sp.getString("saved_key", "")));
                    String encrypted_userName = mahEncryptor.encode("");
                    String encrypted_userPW = mahEncryptor.encode("");

                    BookmarkList db = new BookmarkList(context);
                    db.open();
                    if (db.isExist(url)){
                        NinjaToast.show(context,R.string.toast_newTitle);
                    } else {
                        db.insert(HelperUnit.secString(ninjaWebView.getTitle()), url, encrypted_userName, encrypted_userPW, "01");
                        NinjaToast.show(context,R.string.toast_edit_successful);
                        initBookmarkList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    NinjaToast.show(context,R.string.toast_error);
                }
                break;

            case R.id.submenu_savefavorite:
                popupSubmenuSave.dismiss();
                action.open(true);
                if (action.checkGridItem(url)) {
                    NinjaToast.show(context,R.string.toast_already_exist_in_favorite);
                } else {

                    int counter = sp.getInt("counter", 0);
                    counter = counter + 1;
                    sp.edit().putInt("counter", counter).commit();
                    Bitmap bitmap = ViewUnit.capture(ninjaWebView, dimen156dp, dimen117dp, Bitmap.Config.ARGB_8888);
                    String filename = counter + BrowserUnit.SUFFIX_PNG;
                    GridItem itemAlbum = new GridItem(title, url, filename, counter);

                    if (BrowserUnit.bitmap2File(context, bitmap, filename) && action.addGridItem(itemAlbum)) {
                        NinjaToast.show(context,R.string.toast_add_to_favorite_successful);
                    } else {
                        NinjaToast.show(context,R.string.toast_add_to_favorite_failed);
                    }
                }
                action.close();
                break;

                // Omnibox

            case R.id.popupmenu_search_website:
            case R.id.popupmenu_search_websitew:
                if(isWintan)
                    popupMainMenuW.dismiss();
                else
                    popupMainMenu.dismiss();
                hideKeyboard(activity);
                showSearchPanel();
                break;
            case  R.id.action_search_go:
                updateAlbum(inputBox.getText().toString().trim());
                hideUrlPanel();
                break;
            case R.id.toolbarLayout:
                if (!isUIWintan)
                {
                    break;
                }
            case R.id.title:
            case R.id.url:
                //todo
                // urlbar正常显示
                showUrlPanel();
                String s = ninjaWebView.getUrl();
                if(s==null || s.isEmpty()){
                    inputBox.setText(R.string.app_name);
                }
                else {
                    inputBox.setText(s);
                }
                inputBox.selectAll();
                break;
            case R.id.action_scan:
                //todo
                Intent i = new Intent(BrowserActivity.this, io.github.xudaojie.qrcodelib.CaptureActivity.class);
                startActivityForResult(i, REQUEST_QR_CODE);

                break;
            case R.id.submenu_shareQRcode:
                //todo
                // share with qrcode
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap("content", BarcodeFormat.QR_CODE, 400, 400);
                ImageView imageViewQrCode = (ImageView) findViewById(R.id.qrCode);
                imageViewQrCode.setImageBitmap(bitmap);

                AlertDialog.Builder builder = new AlertDialog.Builder(BrowserActivity.this);
                builder.setView(imageViewQrCode);
                builder.setMessage(ninjaWebView.getTitle());
                builder.setPositiveButton(R.string.app_ok, null);
                builder.show();
            } catch(Exception e) {

            }

            case R.id.submenu_contextLink_saveAsPDF:
                popupSubmenuSave.dismiss();
            case R.id.contextLink_saveAs:
                hideBottomSheetDialog ();
                printPDF(false);
                break;

            case R.id.popupmenu_settings:
                if(isWintan)
                    popupMainMenuW.dismiss();
                else
                    popupMainMenu.dismiss();
                settings = new Intent(BrowserActivity.this, Settings_Activity.class);
                startActivity(settings);
                break;

            case R.id.submenu_fileManager:
                popupSubmenuMore.dismiss();
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setType( "*/*");
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent2, null));
                break;

            case R.id.submenu_download:
                popupSubmenuMore.dismiss();
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                break;

            case R.id.popupmenu_other:
                if(isWintan)
                    popupMainMenuW.dismiss();
                else
                    popupMainMenu.dismiss();
                showMenuMore();
                break;

            case R.id.popupmenu_share:
                if(isWintan)
                    popupMainMenuW.dismiss();
                else
                    popupMainMenu.dismiss();
                showMenuShare();
                break;

            case R.id.popupmenu_save:
                if(isWintan)
                    popupMainMenuW.dismiss();
                else
                    popupMainMenu.dismiss();
                showMenuSave();
                break;

            // Buttons


            case R.id.popupmenu_overview:
            case R.id.popupmenu_overvieww:
                if(isWintan)
                    popupMainMenuW.dismiss();
                else
                    popupMainMenu.dismiss();
                showOverview();
                break;

            case R.id.popupmenu_refresh:
                if(!isWintan) popupMainMenu.dismiss();
            case R.id.action_refresh:
                if (url != null && ninjaWebView.isLoadFinish()) {

                    if (!url.startsWith("https://")) {
                        bottomSheetDialog = new BottomSheetDialog(context);
                        View dialogView = View.inflate(context, R.layout.dialog_action, null);
                        TextView textView = dialogView.findViewById(R.id.dialog_text);
                        textView.setText(R.string.toast_unsecured);
                        Button action_ok = dialogView.findViewById(R.id.action_ok);
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideBottomSheetDialog ();
                                ninjaWebView.loadUrl(url.replace("http://", "https://"));
                            }
                        });
                        Button action_cancel2 = dialogView.findViewById(R.id.action_cancel);
                        action_cancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideBottomSheetDialog ();
                                ninjaWebView.reload();
                            }
                        });
                        bottomSheetDialog.setContentView(dialogView);
                        bottomSheetDialog.show();
                        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        ninjaWebView.reload();
                    }
                } else if (url == null ){
                    String text = getString(R.string.toast_load_error) + ": " + url;
                    NinjaToast.show(context,text);
                } else {
                    ninjaWebView.stopLoading();
                    //todo
                    updateOmnibox();
                }
                break;

            default:
                break;
        }
    }

    // Methods

    private void printPDF (boolean share) {

        try {
            if (share) {
                sp.edit().putBoolean("pdf_share", true).commit();
            } else {
                sp.edit().putBoolean("pdf_share", false).commit();
            }

            String title = HelperUnit.fileName(ninjaWebView.getUrl());
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printAdapter = ninjaWebView.createPrintDocumentAdapter(title);
            Objects.requireNonNull(printManager).print(title, printAdapter, new PrintAttributes.Builder().build());
            sp.edit().putBoolean("pdf_create", true).commit();

        } catch (Exception e) {
            NinjaToast.show(context,R.string.toast_error);
            sp.edit().putBoolean("pdf_create", false).commit();
            e.printStackTrace();
        }
    }


    private void dispatchIntent(Intent intent) {

        String action = intent.getAction();
        String url = intent.getStringExtra(Intent.EXTRA_TEXT);

        if ("".equals(action)) {
            Log.i(TAG, "resumed FOSS browser");
        } else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_WEB_SEARCH)) {
            addAlbum(null, intent.getStringExtra(SearchManager.QUERY), true);
        } else if (filePathCallback != null) {
            filePathCallback = null;
        } else if ("sc_history".equals(action)) {
            addAlbum(null, sp.getString("favoriteURL", "https://github.com/scoute-dich/browser"), true);
            showCollections();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    open_history.performClick();
                }
            }, 250);
        } else if ("sc_bookmark".equals(action)) {
            addAlbum(null, sp.getString("favoriteURL", "https://github.com/scoute-dich/browser"), true);
            showCollections();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    open_bookmark.performClick();
                }
            }, 250);
        } else if ("sc_startPage".equals(action)) {
            addAlbum(null, sp.getString("favoriteURL", "https://github.com/scoute-dich/browser"), true);
            showCollections();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    open_favorite.performClick();
                }
            }, 250);
        } else if (Intent.ACTION_SEND.equals(action)) {
            addAlbum(null, url, true);
        } else {
            if (!onPause) {
                addAlbum(null, sp.getString("favoriteURL", "https://github.com/scoute-dich/browser"), true);
            }
        }
        getIntent().setAction("");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOmnibox() {

        ViewStub vs_toolbar = findViewById(R.id.vs_toolbar);
        ViewStub vs_toolbar_rtl = findViewById(R.id.vs_toolbar_rtl);
        if (rtl) {
            vs_toolbar_rtl.inflate();
        } else{
            vs_toolbar.inflate();
        }

        toolbar=findViewById(R.id.toolbarLayout);//omnibox

        normalCenterLayout=findViewById(R.id.normalcenterLayout);
        centerLayout =findViewById(R.id.centerLayout);

        TitleTv = findViewById(R.id.Title);
        titleTv = findViewById(R.id.title);
        urlTv =  findViewById(R.id.url);

        close =  findViewById(R.id.close);
        back = findViewById(R.id.back);
        forward = findViewById(R.id.forward);
        more = findViewById(R.id.more);

        history_refresh = findViewById(R.id.action_refresh);
        bookmark_home = findViewById(R.id.action_home);

        progressBar = findViewById(R.id.main_progress_bar);

        titleTv.setOnClickListener(this);
        urlTv.setOnClickListener(this);
        toolbar.setOnClickListener(this);

        close.setOnClickListener(this);
        back.setOnClickListener(this);
        forward.setOnClickListener(this);

        history_refresh.setOnClickListener(this);
        bookmark_home.setOnClickListener(this);

        //longclick to history
        history_refresh.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
              //todo
                showCollections();
                if(!CollectionTab.equals(getString(R.string.album_title_history)))
                    open_history.performClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 250);

                return false;
            }
        });
        //longclick to bookmark
        bookmark_home.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //todo
                showCollections();
                if(!CollectionTab.equals(getString(R.string.album_title_bookmarks)))
                    open_bookmark.performClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 250);

                return false;
            }
        });


        ViewStub vs_urlbar = findViewById(R.id.vs_urlbar);
        ViewStub vs_urlbar_rtl = findViewById(R.id.vs_urlbar_rtl);
        if (rtl) {
            vs_urlbar_rtl.inflate();
        } else{
            vs_urlbar.inflate();
        }
        urlbar=findViewById(R.id.urlbar);
        inputBox = findViewById(R.id.main_omnibox_input);
        search_go = findViewById(R.id.action_search_go);
        search_chooseEngine = findViewById(R.id.action_search_chooseEngine);
        qrcode_scan = findViewById(R.id.action_scan);

        search_go.setOnClickListener(this);
        qrcode_scan.setOnClickListener(this);
        search_chooseEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final String query = inputBox.getText().toString().trim();
            if (query.isEmpty()) {
                NinjaToast.show(context, getString(R.string.toast_input_empty));
                return;
            }
            final CharSequence[] options = getResources().getStringArray(R.array.setting_entries_search_engine);
            new AlertDialog.Builder(activity)
                .setTitle(R.string.setting_title_search_engine)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        updateAlbum( geturlwithsearchengine(query,item));
                        hideUrlPanel();
                    }
                }).show();
            }
        });


        String nav_position = Objects.requireNonNull(sp.getString("nav_position", "0"));

        switch (nav_position) {
            case "1":
                fab_imageButtonNav = findViewById(R.id.fab_imageButtonNav_left);
                break;
            case "2":
                fab_imageButtonNav = findViewById(R.id.fab_imageButtonNav_center);
                break;
            default:
                fab_imageButtonNav = findViewById(R.id.fab_imageButtonNav_right);
                break;
        }

        fab_imageButtonNav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                show_dialogFastToggle();
                return false;
            }
        });


        fab_imageButtonNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainMenu(v);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainMenu(v);
            }
        });
        more.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                show_dialogFastToggle();
                return false;
            }
        });

        if (sp.getBoolean("sp_gestures_use", true)) {
            fab_imageButtonNav.setOnTouchListener(new SwipeTouchListener(context) {
                public void onSwipeTop() { performGesture("setting_gesture_nav_up"); }
                public void onSwipeBottom() { performGesture("setting_gesture_nav_down"); }
                public void onSwipeRight() { performGesture("setting_gesture_nav_right"); }
                public void onSwipeLeft() { performGesture("setting_gesture_nav_left"); }
            });

            more.setOnTouchListener(new SwipeTouchListener(context) {
                public void onSwipeTop() { performGesture("setting_gesture_nav_up"); }
                public void onSwipeBottom() { performGesture("setting_gesture_nav_down"); }
                public void onSwipeRight() { performGesture("setting_gesture_nav_right"); }
                public void onSwipeLeft() { performGesture("setting_gesture_nav_left"); }
            });

            toolbar.setOnTouchListener(new SwipeTouchListener(context) {
                public void onSwipeTop() { performGesture("setting_gesture_tb_up"); }
                public void onSwipeBottom() { performGesture("setting_gesture_tb_down"); }
                public void onSwipeRight() { performGesture("setting_gesture_tb_right"); }
                public void onSwipeLeft() { performGesture("setting_gesture_tb_left"); }
            });
        }

        inputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_GO)) {
                    String query = inputBox.getText().toString().trim();
                    if (query.isEmpty()) {
                        NinjaToast.show(context, getString(R.string.toast_input_empty));
                        return true;
                    }
                    search_go.performClick();
                }
                return false;
            }
        });

        inputBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (inputBox.hasFocus()) {
                    ninjaWebView.stopLoading();
                    inputBox.setText(ninjaWebView.getUrl());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            inputBox.selectAll();
                        }
                    }, 250);
                } else {
                    //TODO
                    hideUrlPanel();
                    TitleTv.setText(ninjaWebView.getTitle());
                    titleTv.setText(ninjaWebView.getTitle());
                    urlTv.setText(ninjaWebView.getUrl());
                }
            }
        });

        updateAutoComplete();
    }
    private String geturlwithsearchengine(String query,int item){
        switch (item) {
            case 0:
                return "https://startpage.com/do/search?query=" + query;
            case 1:
                return "https://startpage.com/do/search?lui=deu&language=deutsch&query=" + query;
            case 2:
                return "https://www.baidu.com/s?wd=" + query;
            case 3:
                return "http://www.bing.com/search?q=" + query;
            case 4:
                return "https://duckduckgo.com/?q=" + query;
            case 5:
                return "https://www.google.com/search?q=" + query;
            case 6:
                return "https://searx.me/?q=" + query;
            case 7:
                return "https://www.qwant.com/?q=" + query;
            case 8:
                return "https://www.ecosia.org/search?q=" + query;
            case 9:
                String custom = sp.getString(context.getString(R.string.sp_search_engine_custom), "https://www.baidu.com/s?wd=");
                return custom + query;
            default:
              return query;
        }
    }
    private void performGesture (String gesture) {
        String gestureAction = Objects.requireNonNull(sp.getString(gesture, "0"));
        AlbumController controller;
        ninjaWebView = (NinjaWebView) currentAlbumController;

        switch (gestureAction) {
            case "01":

                break;
            case "02":
                if (ninjaWebView.canGoForward()) {
                    ninjaWebView.goForward();
                } else {
                    NinjaToast.show(context,R.string.toast_webview_forward);
                }
                break;
            case "03":
                goback();
                break;
            case "04":
                ninjaWebView.pageUp(true);
                break;
            case "05":
                ninjaWebView.pageDown(true);
                break;
            case "06":
                controller = nextAlbumController(false);
                showAlbum(controller);
                break;
            case "07":
                controller = nextAlbumController(true);
                showAlbum(controller);
                break;
            case "08":
                showOverview();
                break;
            case "09":
                addAlbum(getString(R.string.app_name), sp.getString("favoriteURL", "https://github.com/scoute-dich/browser"), true);
                break;
            case "10":
                removeAlbum(currentAlbumController);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMenu() {
        //popupmenu main
        View menuviewW = LayoutInflater.from(this).inflate(R.layout.popupmenuw, null);
        popupMainMenuW = new PopupWindow(menuviewW);
        popupMainMenuW.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMainMenuW.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMainMenuW.setContentView(menuviewW);
        popupMainMenuW.setFocusable(true);
        popupMainMenuW.setTouchable(true);
        popupMainMenuW.setOutsideTouchable(true);
        popupMainMenuW.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        if (Build.VERSION.SDK_INT >= 21)
            popupMainMenuW.setElevation(getResources().getDimension(R.dimen.menu_elevation));
        popupMainMenuW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupmenu_normalW = menuviewW.findViewById(R.id.popupmenu_normalw);
        popupmenu_backW = menuviewW.findViewById(R.id.popupmenu_backw);
        popupmenu_search_websiteW = menuviewW.findViewById(R.id.popupmenu_search_websitew);
        popupmenu_overvieww = menuviewW.findViewById(R.id.popupmenu_overvieww);
        popupmenu_addBookmarkw = menuviewW.findViewById(R.id.popupmenu_addBookmarkw);
        popupmenu_forwardW = menuviewW.findViewById(R.id.popupmenu_forwardw);
        popupmenu_quitW = menuviewW.findViewById(R.id.popupmenu_quit);

        popupmenu_normalW.setOnClickListener(BrowserActivity.this);
        popupmenu_backW.setOnClickListener(BrowserActivity.this);
        popupmenu_search_websiteW.setOnClickListener(BrowserActivity.this);
        popupmenu_overvieww.setOnClickListener(BrowserActivity.this);
        popupmenu_addBookmarkw.setOnClickListener(BrowserActivity.this);
        popupmenu_forwardW.setOnClickListener(BrowserActivity.this);
        popupmenu_quitW.setOnClickListener(this);
        /*
        fab_share = dialogView.findViewById(R.id.floatButton_share);
        fab_share.setOnClickListener(BrowserActivity.this);
        fab_save = dialogView.findViewById(R.id.floatButton_save);
        fab_save.setOnClickListener(BrowserActivity.this);
        fab_more = dialogView.findViewById(R.id.floatButton_more);
        fab_more.setOnClickListener(BrowserActivity.this);

        floatButton_tabView = dialogView.findViewById(R.id.floatButton_tabView);
        floatButton_saveView = dialogView.findViewById(R.id.floatButton_saveView);
        floatButton_shareView = dialogView.findViewById(R.id.floatButton_shareView);
        floatButton_moreView = dialogView.findViewById(R.id.floatButton_moreView);



        menu_searchSite = dialogView.findViewById(R.id.menu_searchSite);
        menu_searchSite.setOnClickListener(BrowserActivity.this);
        menu_settings = dialogView.findViewById(R.id.menu_settings);
        menu_settings.setOnClickListener(BrowserActivity.this);
      */

        //popupmenu main for Wintan
        View menuview = LayoutInflater.from(this).inflate(R.layout.popupmenu, null);
        popupMainMenu = new PopupWindow(menuview);
        popupMainMenu.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMainMenu.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMainMenu.setContentView(menuview);
        popupMainMenu.setFocusable(true);
        popupMainMenu.setTouchable(true);
        popupMainMenu.setOutsideTouchable(true);
        popupMainMenu.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        if (Build.VERSION.SDK_INT >= 21)
            popupMainMenu.setElevation(getResources().getDimension(R.dimen.menu_elevation));
        popupMainMenu.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupmenu_wintan = menuview.findViewById(R.id.popupmenu_wintan);
        popupmenu_overview = menuview.findViewById(R.id.popupmenu_overview);
        popupmenu_homepage= menuview.findViewById(R.id.popupmenu_homepage);
        //popupmenu_showFavorite = menuview.findViewById(R.id.popupmenu_showfavorite);
        popupmenu_addBookmark = menuview.findViewById(R.id.popupmenu_addBookmark);
        popupmenu_refresh = menuview.findViewById(R.id.popupmenu_refresh);
        popupmenu_search_website = menuview.findViewById(R.id.popupmenu_search_website);

        popupmenu_wintan.setOnClickListener(this);
        popupmenu_search_website.setOnClickListener(this);
        //popupmenu_showFavorite.setOnClickListener(this);
        popupmenu_overview.setOnClickListener(this);
        popupmenu_homepage.setOnClickListener(this);
        popupmenu_addBookmark.setOnClickListener(this);
        popupmenu_refresh.setOnClickListener(this);

        //popupmenu main for Wintan&YCY
        popups =menuview.findViewById(R.id.mainmenu_item);
        popupsW =menuviewW.findViewById(R.id.mainmenu_itemW);


        popupmenu_other = popups.findViewById(R.id.popupmenu_other);
        popupmenu_save = popups.findViewById(R.id.popupmenu_save);
        popupmenu_share = popups.findViewById(R.id.popupmenu_share);
        //popupmenu_quit = popups.findViewById(R.id.popupmenu_quit);
        popupmenu_settings = popups.findViewById(R.id.popupmenu_settings);

        popupmenu_save.setOnClickListener(this);
        popupmenu_share.setOnClickListener(this);
        popupmenu_settings.setOnClickListener(this);
        popupmenu_other.setOnClickListener(this);
        //popupmenu_quit.setOnClickListener(this);

        popupmenu_otherW = popupsW.findViewById(R.id.popupmenu_other);
        popupmenu_saveW = popupsW.findViewById(R.id.popupmenu_save);
        popupmenu_shareW = popupsW.findViewById(R.id.popupmenu_share);
        popupmenu_settingsW = popupsW.findViewById(R.id.popupmenu_settings);

        popupmenu_saveW.setOnClickListener(this);
        popupmenu_shareW.setOnClickListener(this);
        popupmenu_settingsW.setOnClickListener(this);
        popupmenu_otherW.setOnClickListener(this);

        //popupmenu more
        View submenumoreview = View.inflate(context,R.layout.popupsubmenu_more, null);
        popupSubmenuMore = new PopupWindow(submenumoreview);
        popupSubmenuMore.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupSubmenuMore.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupSubmenuMore.setContentView(submenumoreview);
        popupSubmenuMore.setFocusable(true);
        popupSubmenuMore.setTouchable(true);
        popupSubmenuMore.setOutsideTouchable(true);
        popupSubmenuMore.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        if (Build.VERSION.SDK_INT >= 21)
            popupSubmenuMore.setElevation(getResources().getDimension(R.dimen.menu_elevation));
        popupSubmenuMore.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        menu_newTabOpen = submenumoreview.findViewById(R.id.submenu_newTabOpen);
        menu_newTabOpen.setOnClickListener(BrowserActivity.this);
        menu_closeTab = submenumoreview.findViewById(R.id.submenu_closeTab);
        menu_closeTab.setOnClickListener(BrowserActivity.this);
        menu_download = submenumoreview.findViewById(R.id.submenu_download);
        menu_download.setOnClickListener(BrowserActivity.this);
        menu_fileManager = submenumoreview.findViewById(R.id.submenu_fileManager);
        menu_fileManager.setOnClickListener(BrowserActivity.this);

        menu_openFavorite = submenumoreview.findViewById(R.id.submenu_openFavorite);
        menu_openFavorite.setOnClickListener(this);
        menu_openBookmark = submenumoreview.findViewById(R.id.submenu_openBookmark);
        menu_openBookmark.setOnClickListener(BrowserActivity.this);
        menu_openHistory = submenumoreview.findViewById(R.id.submenu_openHistory);
        menu_openHistory.setOnClickListener(BrowserActivity.this);

        //popupmenu save
        View submenusaveview = View.inflate(context, R.layout.popupsubmenu_save, null);
        popupSubmenuSave = new PopupWindow(submenusaveview);
        popupSubmenuSave.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupSubmenuSave.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupSubmenuSave.setContentView(submenusaveview);
        popupSubmenuSave.setFocusable(true);
        popupSubmenuSave.setTouchable(true);
        popupSubmenuSave.setOutsideTouchable(true);
        popupSubmenuSave.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        if (Build.VERSION.SDK_INT >= 21)
            popupSubmenuSave.setElevation(getResources().getDimension(R.dimen.menu_elevation));
        popupSubmenuSave.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        menu_saveScreenshot = submenusaveview.findViewById(R.id.submenu_saveScreenshot);
        menu_saveScreenshot.setOnClickListener(BrowserActivity.this);
        menu_saveBookmark = submenusaveview.findViewById(R.id.submenu_saveBookmark);
        menu_saveBookmark.setOnClickListener(BrowserActivity.this);
        menu_savePDF = submenusaveview.findViewById(R.id.submenu_contextLink_saveAsPDF);
        menu_savePDF.setOnClickListener(BrowserActivity.this);
        menu_saveStart = submenusaveview.findViewById(R.id.submenu_savefavorite);
        menu_saveStart.setOnClickListener(BrowserActivity.this);


        menu_shortcut = submenusaveview.findViewById(R.id.submenu_shortcut);
        menu_shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSubmenuSave.dismiss();
                HelperUnit.createShortcut(context, ninjaWebView.getTitle(), ninjaWebView.getUrl());
            }
        });
        menu_homepage = submenusaveview.findViewById(R.id.submenu_homepage);
        menu_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSubmenuSave.dismiss();
                HelperUnit.setFavorite(context, url);
            }
        });

        //popupmenu share
        View submenushareview = View.inflate(context, R.layout.popupsubmenu_share, null);
        popupSubmenuShare = new PopupWindow(submenushareview);
        popupSubmenuShare.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupSubmenuShare.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupSubmenuShare.setContentView(submenushareview);
        popupSubmenuShare.setFocusable(true);
        popupSubmenuShare.setTouchable(true);
        popupSubmenuShare.setOutsideTouchable(true);
        popupSubmenuShare.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        if (Build.VERSION.SDK_INT >= 21)
            popupSubmenuShare.setElevation(getResources().getDimension(R.dimen.menu_elevation));
        popupSubmenuShare.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);



        menu_shareScreenshot = submenushareview.findViewById(R.id.submenu_shareScreenshot);
        menu_shareScreenshot.setOnClickListener(BrowserActivity.this);
        menu_shareLink = submenushareview.findViewById(R.id.submenu_shareLink);
        menu_shareLink.setOnClickListener(BrowserActivity.this);
        menu_sharePDF = submenushareview.findViewById(R.id.submenu_sharePDF);
        menu_sharePDF.setOnClickListener(BrowserActivity.this);
        menu_openWith = submenushareview.findViewById(R.id.submenu_openWith);
        menu_openWith.setOnClickListener(BrowserActivity.this);


        menu_shareCP = submenushareview.findViewById(R.id.submenu_shareClipboard);
        menu_shareCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSubmenuShare.dismiss();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", url);
                Objects.requireNonNull(clipboard).setPrimaryClip(clip);
                NinjaToast.show(context, R.string.toast_copy_successful);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOverview() {
        bottomSheetDialog_OverView = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_overview, null);
        //todo
        ViewStub vs_toolbar = dialogView.findViewById(R.id.vs_dialog_overview_toolbar);
        ViewStub vs_toolbar_rtl = dialogView.findViewById(R.id.vs_dialog_overview_toolbar_rtl);

        if (rtl) {
            vs_toolbar_rtl.inflate();
        } else{
            vs_toolbar.inflate();
        }

        tab_container = dialogView.findViewById(R.id.tab_container);
        tab_plus = dialogView.findViewById(R.id.tab_plus);
        tab_plus.setOnClickListener(this);

        tab_ScrollView = dialogView.findViewById(R.id.tab_ScrollView);

        bottomSheetDialog_OverView.setContentView(dialogView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCollections() {
        bottomSheetDialog_Records = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_collections, null);
        ViewStub vs_toolbar = dialogView.findViewById(R.id.vs_dialog_collections_toolbar);
        ViewStub vs_toolbar_rtl = dialogView.findViewById(R.id.vs_dialog_collections_toolbar_rtl);

        if (rtl) {
            vs_toolbar_rtl.inflate();
        } else{
            vs_toolbar.inflate();
        }

        open_favorite = dialogView.findViewById(R.id.open_favorite);
        open_bookmark = dialogView.findViewById(R.id.open_bookmark);
        open_history = dialogView.findViewById(R.id.open_history);
        open_menu = dialogView.findViewById(R.id.open_menu);

        listView = dialogView.findViewById(R.id.home_list_2);
        gridView = dialogView.findViewById(R.id.home_grid_2);

        open_favoriteView = dialogView.findViewById(R.id.open_favoriteView);
        open_bookmarkView = dialogView.findViewById(R.id.open_bookmarkView);
        open_historyView = dialogView.findViewById(R.id.open_historyView);

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {// Disallow NestedScrollView to intercept touch events.
                    if (listView.canScrollVertically(-1)) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        gridView.setOnTouchListener(new ListView.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {// Disallow NestedScrollView to intercept touch events.
                    if (gridView.canScrollVertically(-1)) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        open_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context);
                View dialogView = View.inflate(context, R.layout.dialog_menu_overview, null);

                LinearLayout bookmark_sort = dialogView.findViewById(R.id.bookmark_sort);
                LinearLayout bookmark_filter = dialogView.findViewById(R.id.bookmark_filter);

                if (CollectionTab.equals(getString(R.string.album_title_bookmarks))) {
                    bookmark_filter.setVisibility(View.VISIBLE);
                    bookmark_sort.setVisibility(View.VISIBLE);
                } else if (CollectionTab.equals(getString(R.string.album_title_favorite))){
                    bookmark_filter.setVisibility(View.GONE);
                    bookmark_sort.setVisibility(View.VISIBLE);
                } else if (CollectionTab.equals(getString(R.string.album_title_history))){
                    bookmark_filter.setVisibility(View.GONE);
                    bookmark_sort.setVisibility(View.GONE);
                }

                bookmark_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show_dialogFilter();
                    }
                });

                bookmark_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideBottomSheetDialog ();
                        bottomSheetDialog = new BottomSheetDialog(context);
                        View dialogView = View.inflate(context, R.layout.dialog_bookmark_sort, null);
                        LinearLayout dialog_sortName = dialogView.findViewById(R.id.dialog_sortName);
                        TextView bookmark_sort_tv = dialogView.findViewById(R.id.bookmark_sort_tv);

                        if (CollectionTab.equals(getString(R.string.album_title_bookmarks))) {
                            bookmark_sort_tv.setText(getResources().getString(R.string.dialog_sortIcon));
                        } else if (CollectionTab.equals(getString(R.string.album_title_favorite))){
                            bookmark_sort_tv.setText(getResources().getString(R.string.dialog_sortDate));
                        }

                        dialog_sortName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (CollectionTab.equals(getString(R.string.album_title_bookmarks))) {
                                    sp.edit().putString("sortDBB", "title").apply();
                                    initBookmarkList();
                                    hideBottomSheetDialog ();
                                } else if (CollectionTab.equals(getString(R.string.album_title_favorite))){
                                    sp.edit().putString("sort_startSite", "title").apply();
                                    open_favorite.performClick();
                                    hideBottomSheetDialog ();
                                }
                            }
                        });
                        LinearLayout dialog_sortIcon = dialogView.findViewById(R.id.dialog_sortIcon);
                        dialog_sortIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (CollectionTab.equals(getString(R.string.album_title_bookmarks))) {
                                    sp.edit().putString("sortDBB", "icon").apply();
                                    initBookmarkList();
                                    hideBottomSheetDialog ();
                                } else if (CollectionTab.equals(getString(R.string.album_title_favorite))){
                                    sp.edit().putString("sort_startSite", "ordinal").apply();
                                    open_favorite.performClick();
                                    hideBottomSheetDialog ();
                                }

                            }
                        });
                        bottomSheetDialog.setContentView(dialogView);
                        bottomSheetDialog.show();
                        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                    }
                });

                LinearLayout tv_delete = dialogView.findViewById(R.id.tv_delete);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideBottomSheetDialog ();
                        bottomSheetDialog = new BottomSheetDialog(context);
                        View dialogView3 = View.inflate(context, R.layout.dialog_action, null);
                        TextView textView = dialogView3.findViewById(R.id.dialog_text);
                        textView.setText(R.string.hint_database);
                        Button action_ok = dialogView3.findViewById(R.id.action_ok);
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (CollectionTab.equals(getString(R.string.album_title_favorite))) {
                                    BrowserUnit.clearHome(context);
                                    open_favorite.performClick();
                                } else if (CollectionTab.equals(getString(R.string.album_title_bookmarks))) {
                                    File data = Environment.getDataDirectory();
                                    String bookmarksPath_app = "//data//" + getPackageName() + "//databases//pass_DB_v01.db";
                                    final File bookmarkFile_app = new File(data, bookmarksPath_app);
                                    BrowserUnit.deleteDir(bookmarkFile_app);
                                    open_bookmark.performClick();
                                } else if (CollectionTab.equals(getString(R.string.album_title_history))) {
                                    BrowserUnit.clearHistory(context);
                                    open_history.performClick();
                                }
                                hideBottomSheetDialog ();
                            }
                        });
                        Button action_cancel = dialogView3.findViewById(R.id.action_cancel);
                        action_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideBottomSheetDialog ();
                            }
                        });
                        bottomSheetDialog.setContentView(dialogView3);
                        bottomSheetDialog.show();
                        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView3, BottomSheetBehavior.STATE_EXPANDED);
                    }
                });

                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        bottomSheetDialog_Records.setContentView(dialogView);

        mBehavior = BottomSheetBehavior.from((View) dialogView.getParent());
        //int peekHeight = Math.round(200 * getResources().getDisplayMetrics().density);
        //mBehavior.setPeekHeight(peekHeight);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    hideCollections();
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED){

                    if (CollectionTab.equals(getString(R.string.album_title_bookmarks))) {
                        initBookmarkList();
                    } else if (CollectionTab.equals(getString(R.string.album_title_favorite))) {
                        open_favorite.performClick();
                    }

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        open_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                open_favoriteView.setVisibility(View.VISIBLE);
                open_bookmarkView.setVisibility(View.INVISIBLE);
                open_historyView.setVisibility(View.INVISIBLE);

                CollectionTab = getString(R.string.album_title_favorite);

                RecordAction action = new RecordAction(context);
                action.open(false);
                final List<GridItem> gridList = action.listGrid(context);
                action.close();

                gridAdapter = new de.baumann.browser.view.GridAdapter(context, gridList);
                gridView.setAdapter(gridAdapter);
                gridAdapter.notifyDataSetChanged();

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        updateAlbum(gridList.get(position).getURL());
                        hideCollections();
                    }
                });

                gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        show_contextMenu_list(gridList.get(position).getTitle(), gridList.get(position).getURL(),
                                null, null, 0,
                                null, null, null , null, gridList.get(position));
                        return true;
                    }
                });
            }
        });

        open_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                open_favoriteView.setVisibility(View.INVISIBLE);
                open_bookmarkView.setVisibility(View.VISIBLE);
                open_historyView.setVisibility(View.INVISIBLE);

                CollectionTab = getString(R.string.album_title_bookmarks);
                sp.edit().putString("filter_bookmarks", "00").apply();
                initBookmarkList();
            }
        });

        open_bookmark.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                show_dialogFilter();
                return false;
            }
        });

        open_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                open_favoriteView.setVisibility(View.INVISIBLE);
                open_bookmarkView.setVisibility(View.INVISIBLE);
                open_historyView.setVisibility(View.VISIBLE);

                CollectionTab = getString(R.string.album_title_history);

                RecordAction action = new RecordAction(context);
                action.open(false);
                final List<Record> list;
                list = action.listEntries(activity, false);
                action.close();

                adapter = new Adapter_Record(context, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        updateAlbum(list.get(position).getURL());
                        hideCollections();
                    }
                });

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        show_contextMenu_list(list.get(position).getTitle(), list.get(position).getURL(), adapter, list, position,
                                null, null, null , null, null);
                        return true;
                    }
                });
            }
        });


        switch (Objects.requireNonNull(sp.getString("start_tab", "0"))) {

            case "3":
                open_bookmark.performClick();
                break;
            case "4":
                open_history.performClick();
                break;
            default:
                open_favorite.performClick();
                break;
        }
    }

    private void initSearchPanel() {
        searchPanel = findViewById(R.id.main_search_panel);
        searchBox = findViewById(R.id.main_search_box);
        searchUp = findViewById(R.id.main_search_up);
        searchDown = findViewById(R.id.main_search_down);
        searchCancel = findViewById(R.id.main_search_cancel);
        howMatch = findViewById(R.id.main_search_howmatch);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable key) {
                if (currentAlbumController != null) {
                    ((NinjaWebView) currentAlbumController).findAllAsync(key.toString());
                    ((NinjaWebView) currentAlbumController).setFindListener(new NinjaWebView.FindListener() {
                        @Override
                        public void onFindResultReceived(int position, int all, boolean b) {
                            howMatch.setText("[ "+(position+1)+"/"+all+" ]");
                        }
                    });
                }
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_DONE) {
                    return false;
                }
                if (searchBox.getText().toString().isEmpty()) {
                    NinjaToast.show(context, getString(R.string.toast_input_empty));
                    return true;
                }
                return false;
            }
        });

        searchUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBox.getText().toString();
                if (query.isEmpty()) {
                    NinjaToast.show(context, getString(R.string.toast_input_empty));
                    return;
                }
                hideKeyboard(activity);
                ((NinjaWebView) currentAlbumController).findNext(false);
            }
        });

        searchDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBox.getText().toString();
                if (query.isEmpty()) {
                    NinjaToast.show(context, getString(R.string.toast_input_empty));
                    return;
                }
                hideKeyboard(activity);
                ((NinjaWebView) currentAlbumController).findNext(true);
            }
        });

        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearchPanel();
            }
        });
    }



    private void initBookmarkList() {

        final BookmarkList db = new BookmarkList(context);
        final Cursor row;
        db.open();

        final int layoutStyle = R.layout.list_item_bookmark;
        int[] xml_id = new int[] {
                R.id.record_item_title
        };
        String[] column = new String[] {
                "pass_title",
        };

        String search = sp.getString("filter_bookmarks", "00");

        if (Objects.requireNonNull(search).equals("00")) {
            row = db.fetchAllData(activity);
        } else {
            row = db.fetchDataByFilter(search, "pass_creation");
        }

        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, layoutStyle, row, column, xml_id, 0) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                Cursor row = (Cursor) listView.getItemAtPosition(position);
                final String bookmarks_icon = row.getString(row.getColumnIndexOrThrow("pass_creation"));

                View v = super.getView(position, convertView, parent);
                ImageView iv_icon = v.findViewById(R.id.ib_icon);
                HelperUnit.switchIcon(activity, bookmarks_icon, "pass_creation", iv_icon);

                return v;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final String pass_content = row.getString(row.getColumnIndexOrThrow("pass_content"));
                final String pass_icon = row.getString(row.getColumnIndexOrThrow("pass_icon"));
                final String pass_attachment = row.getString(row.getColumnIndexOrThrow("pass_attachment"));
                updateAlbum(pass_content);
                toast_login (pass_icon, pass_attachment);
                hideCollections();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor row = (Cursor) listView.getItemAtPosition(position);
                final String _id = row.getString(row.getColumnIndexOrThrow("_id"));
                final String pass_title = row.getString(row.getColumnIndexOrThrow("pass_title"));
                final String pass_content = row.getString(row.getColumnIndexOrThrow("pass_content"));
                final String pass_icon = row.getString(row.getColumnIndexOrThrow("pass_icon"));
                final String pass_attachment = row.getString(row.getColumnIndexOrThrow("pass_attachment"));
                final String pass_creation = row.getString(row.getColumnIndexOrThrow("pass_creation"));

                show_contextMenu_list(pass_title, pass_content, null, null, 0,
                        pass_icon, pass_attachment, _id , pass_creation, null);
                return true;
            }
        });
    }

    private void show_dialogFastToggle() {

        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_toggle, null);


        TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
        dialog_title.setText(HelperUnit.domain(ninjaWebView.getUrl()));
        final TextView toggle_tips = dialogView.findViewById(R.id.toggle_tips);


        javaHosts = new Javascript(context);
        cookieHosts = new Cookie(context);
        adBlock = new AdBlock(context);
        ninjaWebView = (NinjaWebView) currentAlbumController;

        final String url = ninjaWebView.getUrl();

        //final int intFontzoomOld=Integer.parseInt(Objects.requireNonNull(sp.getString("sp_fontSize", "100")));
        final int intFontzoomOld=Objects.requireNonNull(sp.getInt("sp_fontSize_seekbar", 100));
        intFontzoom = intFontzoomOld;

        final ImageButton toggle_adblock = dialogView.findViewById(R.id.toggle_adblock);
        final View toggle_adblockView = dialogView.findViewById(R.id.toggle_adblockView);

        final ImageButton toggle_JavaScript = dialogView.findViewById(R.id.toggle_JavaScript);
        final View toggle_JavaScriptView = dialogView.findViewById(R.id.toggle_JavaScriptView);

        final ImageButton toggle_cookies = dialogView.findViewById(R.id.toggle_cookies);
        final View toggle_cookiesView = dialogView.findViewById(R.id.toggle_cookiesView);


        if (sp.getBoolean(getString(R.string.sp_javascript), true)){
            toggle_JavaScriptView.setVisibility(View.VISIBLE);
        } else {
            toggle_JavaScriptView.setVisibility(View.INVISIBLE);
        }

        if (sp.getBoolean(getString(R.string.sp_ad_block), true)){
            toggle_adblockView.setVisibility(View.VISIBLE);
        } else {
            toggle_adblockView.setVisibility(View.INVISIBLE);
        }

        if (sp.getBoolean(getString(R.string.sp_cookies), true)){
            toggle_cookiesView.setVisibility(View.VISIBLE);
        } else {
            toggle_cookiesView.setVisibility(View.INVISIBLE);
        }


        toggle_JavaScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getBoolean(getString(R.string.sp_javascript), true)){
                    toggle_JavaScriptView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_javascript), false).commit();
                    toggle_tips.setText(R.string.text_JavaScript_off_tips);
                }else{
                    toggle_JavaScriptView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_javascript), true).commit();
                    toggle_tips.setText(R.string.text_JavaScript_on_tips);
                }

            }
        });

        toggle_adblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getBoolean(getString(R.string.sp_ad_block), true)){
                    toggle_adblockView.setVisibility(view.INVISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_ad_block), false).commit();
                    toggle_tips.setText(R.string.text_adblock_off_tips);
                }else{
                    toggle_adblockView.setVisibility(view.VISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_ad_block), true).commit();
                    toggle_tips.setText(R.string.text_adblock_on_tips);
                }
            }
        });

        toggle_cookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getBoolean(getString(R.string.sp_cookies), true)){
                    toggle_cookiesView.setVisibility(view.INVISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_cookies), false).commit();
                    toggle_tips.setText(R.string.text_cookie_off_tips);
                }else{
                    toggle_cookiesView.setVisibility(view.VISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_cookies), true).commit();
                    toggle_tips.setText(R.string.text_cookie_on_tips);
                }
            }
        });



        final ImageButton whitelist_ab = dialogView.findViewById(R.id.whitelist_ab);
        final View toggle_adblockWhitelistView = dialogView.findViewById(R.id.toggle_adblockwhitelistview);

        final ImageButton whitelist_Javascript = dialogView.findViewById(R.id.whitelist_Javascript);
        final View toggle_JavaScripWhitelistView = dialogView.findViewById(R.id.toggle_JavaScripwhitelistview);

        final ImageButton whitelist_cookies = dialogView.findViewById(R.id.whitelist_cookies);
        final View toggle_cookiesWhitelistView = dialogView.findViewById(R.id.toggle_cookieswhitelistview);


        if (javaHosts.isInBlacklist(ninjaWebView.getUrl())) {
            toggle_JavaScripWhitelistView.setVisibility(View.VISIBLE);
        } else {
            toggle_JavaScripWhitelistView.setVisibility(View.INVISIBLE);
        }

        whitelist_Javascript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (javaHosts.isInBlacklist(ninjaWebView.getUrl())) {
                    toggle_JavaScripWhitelistView.setVisibility(View.INVISIBLE);
                    javaHosts.removeDomain(HelperUnit.domain(url));
                    toggle_tips.setText(R.string.text_JavaScript_remove_tips);
                } else {
                    toggle_JavaScripWhitelistView.setVisibility(View.VISIBLE);
                    javaHosts.addDomain(HelperUnit.domain(url));
                    toggle_tips.setText(R.string.text_JavaScript_add_tips);
                }
            }
        });

        if (cookieHosts.isInBlacklist(ninjaWebView.getUrl())) {
            toggle_cookiesWhitelistView.setVisibility(View.VISIBLE);
        } else {
            toggle_cookiesWhitelistView.setVisibility(View.INVISIBLE);
        }
        whitelist_cookies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cookieHosts.isInBlacklist(ninjaWebView.getUrl())) {
                    toggle_cookiesWhitelistView.setVisibility(View.INVISIBLE);
                    cookieHosts.removeDomain(HelperUnit.domain(url));
                    toggle_tips.setText(R.string.text_cookie_remove_tips);
                } else {
                    toggle_cookiesWhitelistView.setVisibility(View.VISIBLE);
                    cookieHosts.addDomain(HelperUnit.domain(url));
                    toggle_tips.setText(R.string.text_cookie_add_tips);
                }
            }
        });

        if (adBlock.isWhite(ninjaWebView.getUrl())) {
            toggle_adblockWhitelistView.setVisibility(View.VISIBLE);
        } else {
            toggle_adblockWhitelistView.setVisibility(View.INVISIBLE);
        }
        whitelist_ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adBlock.isWhite(ninjaWebView.getUrl())) {
                    toggle_adblockWhitelistView.setVisibility(View.INVISIBLE);
                    adBlock.removeDomain(HelperUnit.domain(url));
                    toggle_tips.setText(R.string.text_adblock_remove_tips);
                } else {
                    toggle_adblockWhitelistView.setVisibility(View.VISIBLE);
                    adBlock.addDomain(HelperUnit.domain(url));
                    toggle_tips.setText(R.string.text_adblock_add_tips);
                }
            }
        });

        // doer added begin
        final IndicatorSeekBar seekBar=dialogView.findViewById(R.id.textzoom_seek_bar);
        seekBar.setIndicatorTextFormat("${PROGRESS} %");
        seekBar.setProgress(intFontzoom);

        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                intFontzoom=seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                intFontzoom=seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                intFontzoom=seekBar.getProgress();
                toggle_tips.setText(R.string.text_Fontzoom_tips);
            }
        });

        final AppCompatButton zoomout =dialogView.findViewById(R.id.textzoomout);
        final AppCompatButton zoomin =dialogView.findViewById(R.id.textzoomin);
        zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size=seekBar.getProgress();
                seekBar.setProgress(size+1);
                toggle_tips.setText(R.string.text_Fontzoom_tips);
            }
        });
        zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size=seekBar.getProgress();
                seekBar.setProgress(size-1);
                toggle_tips.setText(R.string.text_Fontzoom_tips);
            }
        });
        //doer added end

        final ImageButton toggle_desktop = dialogView.findViewById(R.id.toggle_desktop);
        final View toggle_desktopView = dialogView.findViewById(R.id.toggle_desktopView);
        if (sp.getString(getString(R.string.sp_useragent), "0")=="1") {
            toggle_desktopView.setVisibility(View.VISIBLE);
        } else {
            toggle_desktopView.setVisibility(View.INVISIBLE);
        }

        //todo
        // not work well
        toggle_desktop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getString(getString(R.string.sp_useragent), "0")=="1") {
                    toggle_desktopView.setVisibility(View.INVISIBLE);
                    sp.edit().putString(getString(R.string.sp_useragent), "0").commit();
                    toggle_tips.setText(R.string.text_useagent_default_tips);
                } else {
                    toggle_desktopView.setVisibility(View.VISIBLE);
                    sp.edit().putString(getString(R.string.sp_useragent), "1").commit();
                    toggle_tips.setText(R.string.text_useagent_desktop_tips);
                }
            }
        });

        final ImageButton toggle_history = dialogView.findViewById(R.id.toggle_history);
        final View toggle_historyView = dialogView.findViewById(R.id.toggle_historyView);

        final ImageButton toggle_location = dialogView.findViewById(R.id.toggle_location);
        final View toggle_locationView = dialogView.findViewById(R.id.toggle_locationView);

        final ImageButton toggle_images = dialogView.findViewById(R.id.toggle_images);
        final View toggle_imagesView = dialogView.findViewById(R.id.toggle_imagesView);

        final ImageButton toggle_remote = dialogView.findViewById(R.id.toggle_remote);
        final View toggle_remoteView = dialogView.findViewById(R.id.toggle_remoteView);

        final ImageButton toggle_invert = dialogView.findViewById(R.id.toggle_invert);
        final View toggle_invertView = dialogView.findViewById(R.id.toggle_invertView);

        /* doer deleted
        final ImageButton toggle_font = dialogView.findViewById(R.id.toggle_font);

        toggle_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
                Intent intent = new Intent(context, Settings_Activity.class);
                startActivity(intent);
            }
        });
        */

        if (sp.getBoolean("saveHistory", false)) {
            toggle_historyView.setVisibility(View.VISIBLE);
        } else {
            toggle_historyView.setVisibility(View.INVISIBLE);
        }

        toggle_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean("saveHistory", false)) {
                    toggle_historyView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean("saveHistory", false).commit();
                    toggle_tips.setText(R.string.text_history_ignore_tips);
                } else {
                    toggle_historyView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean("saveHistory", true).commit();
                    toggle_tips.setText(R.string.text_history_record_tips);
                }
            }
        });

        if (sp.getBoolean(getString(R.string.sp_location), false)) {
            toggle_locationView.setVisibility(View.VISIBLE);
        } else {
            toggle_locationView.setVisibility(View.INVISIBLE);
        }

        toggle_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean(getString(R.string.sp_location), false)) {
                    toggle_locationView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_location), false).commit();
                    toggle_tips.setText(R.string.text_location_off_tips);
                } else {
                    toggle_locationView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_location), true).commit();
                    toggle_tips.setText(R.string.text_location_on_tips);
                }
            }
        });

        if (sp.getBoolean(getString(R.string.sp_images), true)) {
            toggle_imagesView.setVisibility(View.VISIBLE);
        } else {
            toggle_imagesView.setVisibility(View.INVISIBLE);
        }

        toggle_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean(getString(R.string.sp_images), true)) {
                    toggle_imagesView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_images), false).commit();
                    toggle_tips.setText(R.string.text_images_hide_tips);
                } else {
                    toggle_imagesView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean(getString(R.string.sp_images), true).commit();
                    toggle_tips.setText(R.string.text_images_show_tips);
                }
            }
        });

        if (sp.getBoolean("sp_remote", true)) {
            toggle_remoteView.setVisibility(View.VISIBLE);
        } else {
            toggle_remoteView.setVisibility(View.INVISIBLE);
        }

        toggle_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean("sp_remote", true)) {
                    toggle_remoteView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean("sp_remote", false).commit();
                    toggle_tips.setText(R.string.text_remote_hide_tips);
                } else {
                    toggle_remoteView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean("sp_remote", true).commit();
                    toggle_tips.setText(R.string.text_remote_show_tips);
                }
            }
        });

        if (sp.getBoolean("sp_invert", false)) {
            toggle_invertView.setVisibility(View.VISIBLE);
        } else {
            toggle_invertView.setVisibility(View.INVISIBLE);
        }

        toggle_invert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean("sp_invert", false)) {
                    toggle_invertView.setVisibility(View.INVISIBLE);
                    sp.edit().putBoolean("sp_invert", false).commit();
                    toggle_tips.setText(R.string.text_invert_off_tips);
                } else {
                    toggle_invertView.setVisibility(View.VISIBLE);
                    sp.edit().putBoolean("sp_invert", true).commit();
                    toggle_tips.setText(R.string.text_invert_on_tips);
                }
                HelperUnit.initRendering(contentFrame);
            }
        });

        Button but_OK = dialogView.findViewById(R.id.action_ok);
        but_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ninjaWebView != null) {
                    if(intFontzoomOld!=intFontzoom)
                    {
                        sp.edit().putInt("sp_fontSize_seekbar",intFontzoom).commit();
                        //sp.edit().putString("sp_fontSize",Integer.toString(intFontzoom)).commit();
                    }

                    ninjaWebView.initPreferences();
                    ninjaWebView.reload();
                }
                hideBottomSheetDialog ();
            }
        });


        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    private void toast_login (String userName, String passWord) {
        try {
            final String decrypted_userName = mahEncryptor.decode(userName);
            final String decrypted_userPW = mahEncryptor.decode(passWord);
            final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            assert clipboard != null;

            final BroadcastReceiver unCopy = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ClipData clip = ClipData.newPlainText("text", decrypted_userName);
                    clipboard.setPrimaryClip(clip);
                    NinjaToast.show(context, R.string.toast_copy_successful);
                }
            };

            final BroadcastReceiver pwCopy = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ClipData clip = ClipData.newPlainText("text", decrypted_userPW);
                    clipboard.setPrimaryClip(clip);
                    NinjaToast.show(context, R.string.toast_copy_successful);
                }
            };

            IntentFilter intentFilter = new IntentFilter("unCopy");
            registerReceiver(unCopy, intentFilter);
            Intent copy = new Intent("unCopy");
            PendingIntent copyUN = PendingIntent.getBroadcast(context, 0, copy, PendingIntent.FLAG_CANCEL_CURRENT);

            IntentFilter intentFilter2 = new IntentFilter("pwCopy");
            registerReceiver(pwCopy, intentFilter2);
            Intent copy2 = new Intent("pwCopy");
            PendingIntent copyPW = PendingIntent.getBroadcast(context, 1, copy2, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder;

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert mNotificationManager != null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "browser_not";// The id of the channel.
                CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(mChannel);
                builder = new NotificationCompat.Builder(context, CHANNEL_ID);
            } else {
                //noinspection deprecation
                builder = new NotificationCompat.Builder(context);
            }

            NotificationCompat.Action action_UN = new NotificationCompat.Action.Builder(R.drawable.icon_earth, getString(R.string.toast_titleConfirm_pasteUN), copyUN).build();
            NotificationCompat.Action action_PW = new NotificationCompat.Action.Builder(R.drawable.icon_earth, getString(R.string.toast_titleConfirm_pastePW), copyPW).build();

            Notification n  = builder
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_notification_ninja)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.toast_titleConfirm_paste))
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[0])
                    .addAction(action_UN)
                    .addAction(action_PW)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;

            if (decrypted_userName.length() > 0 || decrypted_userPW.length() > 0 ) {
                notificationManager.notify(0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            NinjaToast.show(context, R.string.toast_error);
        }
    }


    private synchronized void addAlbum(String title, final String url, final boolean foreground) {

        ninjaWebView = new NinjaWebView(context);
        ninjaWebView.setBrowserController(this);
        ninjaWebView.setAlbumTitle(title);
        ViewUnit.bound(context, ninjaWebView);

        final View albumView = ninjaWebView.getAlbumView();
        if (currentAlbumController != null) {
            int index = BrowserContainer.indexOf(currentAlbumController) + 1;
            BrowserContainer.add(ninjaWebView, index);
            tab_container.addView(albumView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            BrowserContainer.add(ninjaWebView);
            tab_container.addView(albumView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        if (!foreground) {
            ViewUnit.bound(context, ninjaWebView);
            ninjaWebView.loadUrl(url);
            ninjaWebView.deactivate();
            return;
        } else {
            showOmnibox();
            showAlbum(ninjaWebView);
        }

        if (url != null && !url.isEmpty()) {
            ninjaWebView.loadUrl(url);
        }
    }

    private synchronized void updateAlbum(String url) {
        ((NinjaWebView) currentAlbumController).loadUrl(url);
        updateOmnibox();
    }

    private void closeTabConfirmation(final Runnable okAction) {
        if(!sp.getBoolean("sp_close_tab_confirm", false)) {
            okAction.run();
        } else {
            bottomSheetDialog = new BottomSheetDialog(context);
            View dialogView = View.inflate(context, R.layout.dialog_action, null);
            TextView textView = dialogView.findViewById(R.id.dialog_text);
            textView.setText(R.string.toast_close_tab);
            Button action_ok = dialogView.findViewById(R.id.action_ok);
            action_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    okAction.run();
                    hideBottomSheetDialog ();
                }
            });
            Button action_cancel = dialogView.findViewById(R.id.action_cancel);
            action_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideBottomSheetDialog ();
                }
            });
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
            HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public synchronized void removeAlbum(final AlbumController controller) {

        if (BrowserContainer.size() <= 1) {
            if(!sp.getBoolean("sp_reopenLastTab", false)) {
                doubleTapsQuit();
            }else{
                updateAlbum(sp.getString("favoriteURL", "https://github.com/scoute-dich/browser"));
                hideOverview();
            }
        } else {
            closeTabConfirmation( new Runnable() {
                @Override
                public void run() {
                    tab_container.removeView(controller.getAlbumView());
                    int index = BrowserContainer.indexOf(controller);
                    BrowserContainer.remove(controller);
                    if (index >= BrowserContainer.size()) {
                        index = BrowserContainer.size() - 1;
                    }
                    showAlbum(BrowserContainer.get(index));
                }
            });
        }
    }

    private void updateOmnibox() {
        if (ninjaWebView == currentAlbumController) {
            TitleTv.setText(currentAlbumController.getAlbumTitle());
            titleTv.setText(currentAlbumController.getAlbumTitle());
            urlTv.setText(ninjaWebView.getUrl());
        } else {
            ninjaWebView = (NinjaWebView) currentAlbumController;
            updateProgress(ninjaWebView.getProgress());
        }
        { // TextViews
            requestCenterLayout();
        }
    }
    private void requestCenterLayout() {
        int maxWidth;
        if (ninjaWebView.canGoBack() || ninjaWebView.canGoForward()) {
            maxWidth = getResources().getDisplayMetrics().widthPixels - (int) (48* getResources().getDisplayMetrics().density + 0.5f) * 4-4;
        } else {
            maxWidth = getResources().getDisplayMetrics().widthPixels - (int) (48* getResources().getDisplayMetrics().density + 0.5f) * 2-4;
        }
        titleTv.setMaxWidth(maxWidth);
        urlTv.setMaxWidth(maxWidth);
        titleTv.requestLayout();
        urlTv.requestLayout();
    }


    private void scrollChange () {
        if (Objects.requireNonNull(sp.getBoolean("hideToolbar", true))) {
            ninjaWebView.setOnScrollChangeListener(new NinjaWebView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(int scrollY, int oldScrollY) {
                    int height = (int) Math.floor(ninjaWebView.getContentHeight() * ninjaWebView.getResources().getDisplayMetrics().density);
                    int webViewHeight = ninjaWebView.getHeight();
                    int cutoff = height - webViewHeight - 112 * Math.round(getResources().getDisplayMetrics().density);
                    if (scrollY > oldScrollY && cutoff >= scrollY) {
                        hideOmnibox();
                    } else if (scrollY < oldScrollY){
                        showOmnibox();
                    }
                }
            });
        }
    }
    //todo
    @Override
    public synchronized void updateProgress(int progress) {
        progressBar.setProgress(progress);

        if (progress < BrowserUnit.PROGRESS_MAX) {
            if (!isLoading) {
                history_refresh.setImageResource(R.drawable.icon_close);
                progressBar.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        } else {
            updateOmnibox();
            updateAutoComplete();
            scrollChange();
            HelperUnit.initRendering(contentFrame);
            ninjaWebView.requestFocus();
            try {
                if (cantainsreservedstring(ninjaWebView.getUrl())) {
                    history_refresh.setImageResource(R.drawable.icon_history_refresh);
                }
                else {
                    history_refresh.setImageResource(R.drawable.icon_alert);
                }
            } catch (Exception e) {
                history_refresh.setImageResource(R.drawable.icon_history_refresh);
            }
            progressBar.setVisibility(View.GONE);

            // redraw <>
            if (ninjaWebView.canGoBack() || ninjaWebView.canGoForward()) {
                if (isUIWintan) {
                    back.setVisibility(View.GONE);
                    forward.setVisibility(View.GONE);
                }else {
                    back.setVisibility(showIcon ? View.VISIBLE : View.GONE);
                    forward.setVisibility(showIcon ? View.VISIBLE : View.GONE);
                }
                back.setEnabled(!disableIcon && ninjaWebView.canGoBack());
                forward.setEnabled(!disableIcon &&ninjaWebView.canGoForward());
            } else {
                back.setVisibility(View.GONE);
                forward.setVisibility(View.GONE);
            }
            isLoading = false;
        }
    }

    private boolean cantainsreservedstring(String  s) {
        if (s.contains("https://") || s.contains("about:blank"))
            return true;
        else
            return false;
    }
    @Override
    public void showFileChooser(ValueCallback<Uri[]> filePathCallback) {
        if(mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePathCallback;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (view == null) {
            return;
        }
        if (customView != null && callback != null) {
            callback.onCustomViewHidden();
            return;
        }

        customView = view;
        originalOrientation = getRequestedOrientation();

        fullscreenHolder = new FrameLayout(context);
        fullscreenHolder.addView(
                customView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));

        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.addView(
                fullscreenHolder,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));

        customView.setKeepScreenOn(true);
        ((View) currentAlbumController).setVisibility(View.GONE);
        setCustomFullscreen(true);

        if (view instanceof FrameLayout) {
            if (((FrameLayout) view).getFocusedChild() instanceof VideoView) {
                videoView = (VideoView) ((FrameLayout) view).getFocusedChild();
                videoView.setOnErrorListener(new VideoCompletionListener());
                videoView.setOnCompletionListener(new VideoCompletionListener());
            }
        }
        customViewCallback = callback;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean onHideCustomView() {
        if (customView == null || customViewCallback == null || currentAlbumController == null) {
            return false;
        }

        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.removeView(fullscreenHolder);

        customView.setKeepScreenOn(false);
        ((View) currentAlbumController).setVisibility(View.VISIBLE);
        setCustomFullscreen(false);

        fullscreenHolder = null;
        customView = null;
        if (videoView != null) {
            videoView.setOnErrorListener(null);
            videoView.setOnCompletionListener(null);
            videoView = null;
        }
        setRequestedOrientation(originalOrientation);

        return true;
    }

    private void show_contextMenu_link(final String url) {
        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_menu_context_link, null);
        dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(url);

        LinearLayout contextLink_newTab = dialogView.findViewById(R.id.contextLink_newTab);
        contextLink_newTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum(getString(R.string.app_name), url, false);
                NinjaToast.show(context, getString(R.string.toast_new_tab_successful));
                hideBottomSheetDialog ();
            }
        });

        LinearLayout contextLink__shareLink = dialogView.findViewById(R.id.contextLink__shareLink);
        contextLink__shareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prepareRecord()) {
                    NinjaToast.show(context, getString(R.string.toast_share_failed));
                } else {
                    IntentUnit.share(context, "", url);
                }
                hideBottomSheetDialog ();
            }
        });

        LinearLayout contextLink_openWith = dialogView.findViewById(R.id.contextLink_openWith);
        contextLink_openWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                Intent chooser = Intent.createChooser(intent, getString(R.string.menu_open_with));
                startActivity(chooser);
                hideBottomSheetDialog ();
            }
        });

        LinearLayout contextLink_newTabOpen = dialogView.findViewById(R.id.contextLink_newTabOpen);
        contextLink_newTabOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum(getString(R.string.app_name), url, true);
                hideBottomSheetDialog ();
            }
        });

        LinearLayout contextMenu_saveStart = dialogView.findViewById(R.id.contextMenu_saveStart);
        contextMenu_saveStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                RecordAction action = new RecordAction(context);
                action.open(true);
                if (action.checkGridItem(url)) {
                    NinjaToast.show(context, getString(R.string.toast_already_exist_in_favorite));
                } else {

                    int counter = sp.getInt("counter", 0);
                    counter = counter + 1;
                    sp.edit().putInt("counter", counter).commit();

                    Bitmap bitmap = ViewUnit.createImage(3, 3, getResources().getColor(R.color.colorPrimary));
                    String filename = counter + BrowserUnit.SUFFIX_PNG;
                    GridItem itemAlbum = new GridItem(HelperUnit.domain(url), url, filename, counter);

                    if (BrowserUnit.bitmap2File(context, bitmap, filename) && action.addGridItem(itemAlbum)) {
                        NinjaToast.show(context, getString(R.string.toast_add_to_favorite_successful));
                    } else {
                        NinjaToast.show(context, getString(R.string.toast_add_to_favorite_failed));
                    }
                }
                action.close();
            }
        });

        LinearLayout contextLink_sc = dialogView.findViewById(R.id.contextLink_sc);
        contextLink_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                HelperUnit.createShortcut(context, HelperUnit.domain(url), url);
            }
        });

        LinearLayout contextLink_saveAs = dialogView.findViewById(R.id.contextLink_saveAs);
        contextLink_saveAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    hideBottomSheetDialog ();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View dialogView = View.inflate(context, R.layout.dialog_edit_extension, null);

                    final EditText editTitle = dialogView.findViewById(R.id.dialog_edit);
                    final EditText editExtension = dialogView.findViewById(R.id.dialog_edit_extension);

                    String filename = URLUtil.guessFileName(url, null, null);

                    editTitle.setHint(R.string.dialog_title_hint);
                    editTitle.setText(HelperUnit.fileName(ninjaWebView.getUrl()));

                    String extension = filename.substring(filename.lastIndexOf("."));
                    if(extension.length() <= 8) {
                        editExtension.setText(extension);
                    }

                    builder.setView(dialogView);
                    builder.setTitle(R.string.menu_edit);
                    builder.setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            String title = editTitle.getText().toString().trim();
                            String extension = editExtension.getText().toString().trim();
                            String  filename = title + extension;

                            if (title.isEmpty() || extension.isEmpty() || !extension.startsWith(".")) {
                                NinjaToast.show(context, getString(R.string.toast_input_empty));
                            } else {

                                if (android.os.Build.VERSION.SDK_INT >= 23) {
                                    int hasWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                                        HelperUnit.grantPermissionsStorage(activity);
                                    } else {
                                        Uri source = Uri.parse(url);
                                        DownloadManager.Request request = new DownloadManager.Request(source);
                                        request.addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url));
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                        assert dm != null;
                                        dm.enqueue(request);
                                        hideKeyboard(activity);
                                    }
                                } else {
                                    Uri source = Uri.parse(url);
                                    DownloadManager.Request request = new DownloadManager.Request(source);
                                    request.addRequestHeader("Cookie", CookieManager.getInstance().getCookie(url));
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    assert dm != null;
                                    dm.enqueue(request);
                                    hideKeyboard(activity);
                                }
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                            hideKeyboard(activity);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onLongPress(final String url) {
        WebView.HitTestResult result = ninjaWebView.getHitTestResult();
        if (url != null) {
            show_contextMenu_link(url);
        } else if (result.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE ||
                result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
            show_contextMenu_link(result.getExtra());
        }
    }

    private void doubleTapsQuit() {
        if (!sp.getBoolean("sp_close_browser_confirm", true)) {
            finish();
        } else {
            bottomSheetDialog = new BottomSheetDialog(context);
            View dialogView = View.inflate(context, R.layout.dialog_action, null);
            TextView textView = dialogView.findViewById(R.id.dialog_text);
            textView.setText(R.string.toast_quit);
            Button action_ok = dialogView.findViewById(R.id.action_ok);
            action_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            Button action_cancel = dialogView.findViewById(R.id.action_cancel);
            action_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideBottomSheetDialog ();
                }
            });
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
            HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressLint("RestrictedApi")
    private void showOmnibox() {
        if (!searchOnSite)  {
            fab_imageButtonNav.setVisibility(View.GONE);
            //searchPanel.setVisibility(View.GONE);
            //toolbar.setVisibility(View.VISIBLE);
            //urlbar.setVisibility(View.GONE);
            //omniboxTitle.setVisibility(View.VISIBLE);
            appBar.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("RestrictedApi")
    private void hideOmnibox() {
        if (!searchOnSite)  {
            if(sp.getBoolean("sp_navibuttonShow",false)) {
                fab_imageButtonNav.setVisibility(View.VISIBLE);
            }
            //searchPanel.setVisibility(View.GONE);
            //toolbar.setVisibility(View.GONE);
            //omniboxTitle.setVisibility(View.GONE);
            appBar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("RestrictedApi")
    private void showSearchPanel() {
        searchOnSite = true;
        fab_imageButtonNav.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        searchPanel.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    private void hideSearchPanel() {
        searchOnSite = false;
        if(sp.getBoolean("sp_navibuttonShow",false)) {
            fab_imageButtonNav.setVisibility(View.VISIBLE);
        }
        searchBox.setText("");
        searchPanel.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    private void showUrlPanel() {
        fab_imageButtonNav.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        urlbar.setVisibility(View.VISIBLE);
    }
    @SuppressLint("RestrictedApi")
    private void hideUrlPanel() {
        if(sp.getBoolean("sp_navibuttonShow",false)) {
            fab_imageButtonNav.setVisibility(View.VISIBLE);
        }
        urlbar.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        hideKeyboard(activity);
    }

    @SuppressWarnings("SameReturnValue")
    private boolean showMainMenu(View view) {

        if (sp.getBoolean(getString(R.string.sp_wintan_mode),true)) {
            popupmenu_backW.setEnabled(ninjaWebView.canGoBack());
            popupmenu_forwardW.setEnabled(ninjaWebView.canGoForward());
            if (sp.getBoolean(getString(R.string.sp_rtl),false))
                popupMainMenuW.showAtLocation(view, Gravity.START | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
            else
                popupMainMenuW.showAtLocation(view, Gravity.END | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
        }
        else {
            if (sp.getBoolean(getString(R.string.sp_rtl),false))
                popupMainMenu.showAtLocation(view, Gravity.START | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
            else
                popupMainMenu.showAtLocation(view, Gravity.END | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
        }
        return true;
    }

    private boolean showMenuMore() {
        if (sp.getBoolean(getString(R.string.sp_rtl),false))
            popupSubmenuMore.showAtLocation(more, Gravity.START | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
        else
            popupSubmenuMore.showAtLocation(more, Gravity.END | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
        return true;
    }
    private boolean showMenuSave() {
        if (sp.getBoolean(getString(R.string.sp_rtl),false))
            popupSubmenuSave.showAtLocation(more, Gravity.START | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
        else
            popupSubmenuSave.showAtLocation(more, Gravity.END | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
        return true;
    }
    private boolean showMenuShare() {
        if (sp.getBoolean(getString(R.string.sp_rtl),false))
            popupSubmenuShare.showAtLocation(more, Gravity.START | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
        else
            popupSubmenuShare.showAtLocation(more, Gravity.END | Gravity.BOTTOM, 8, appBar.getHeight() + 20);
        return true;
    }

    private void show_contextMenu_list (final String title, final String url,
                                       final Adapter_Record adapterRecord, final List<Record> recordList, final int location,
                                       final String userName, final String userPW, final String _id, final String pass_creation,
                                       final GridItem gridItem) {


        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_menu_context_list, null);

        final BookmarkList db = new BookmarkList(context);
        db.open();

        LinearLayout contextList_edit = dialogView.findViewById(R.id.menu_contextList_edit);
        LinearLayout contextList_homepage = dialogView.findViewById(R.id.menu_contextList_homepage);
        LinearLayout contextList_shortcut = dialogView.findViewById(R.id.menu_contextLink_shortcut);
        LinearLayout contextList_newTab = dialogView.findViewById(R.id.menu_contextList_newTab);
        LinearLayout contextList_newTabOpen = dialogView.findViewById(R.id.menu_contextList_newTabOpen);
        LinearLayout contextList_delete = dialogView.findViewById(R.id.menu_contextList_delete);

        if (CollectionTab.equals(getString(R.string.album_title_history))) {
            contextList_edit.setVisibility(View.GONE);
        } else {
            contextList_edit.setVisibility(View.VISIBLE);
        }

        contextList_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                HelperUnit.setFavorite(context, url);
            }
        });

        contextList_shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                HelperUnit.createShortcut(context, title, url);
            }
        });

        contextList_newTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum(getString(R.string.app_name), url, false);
                NinjaToast.show(context, getString(R.string.toast_new_tab_successful));
                hideBottomSheetDialog ();
            }
        });

        contextList_newTabOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbum(getString(R.string.app_name), url, true);
                hideBottomSheetDialog ();
                hideOverview();
            }
        });

        contextList_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();
                bottomSheetDialog = new BottomSheetDialog(context);
                View dialogView = View.inflate(context, R.layout.dialog_action, null);
                TextView textView = dialogView.findViewById(R.id.dialog_text);
                textView.setText(R.string.toast_titleConfirm_delete);
                Button action_ok = dialogView.findViewById(R.id.action_ok);
                action_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (CollectionTab.equals(getString(R.string.album_title_favorite))) {
                            RecordAction action = new RecordAction(context);
                            action.open(true);
                            action.deleteGridItem(gridItem);
                            action.close();
                            deleteFile(gridItem.getFilename());
                            open_favorite.performClick();
                            hideBottomSheetDialog ();
                        } else if (CollectionTab.equals(getString(R.string.album_title_bookmarks))){
                            db.delete(Integer.parseInt(_id));
                            initBookmarkList();
                            hideBottomSheetDialog ();
                        } else if (CollectionTab.equals(getString(R.string.album_title_history))){
                            Record record = recordList.get(location);
                            RecordAction action = new RecordAction(context);
                            action.open(true);
                            action.deleteHistoryItem(record);
                            action.close();
                            recordList.remove(location);
                            adapterRecord.notifyDataSetChanged();
                            updateAutoComplete();
                            hideBottomSheetDialog ();
                        }

                    }
                });
                Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                action_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideBottomSheetDialog ();
                    }
                });
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();
                HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        contextList_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheetDialog ();

                if (CollectionTab.equals(getString(R.string.album_title_favorite))) {
                    bottomSheetDialog = new BottomSheetDialog(context);
                    View dialogView = View.inflate(context, R.layout.dialog_edit_title, null);

                    final EditText editText = dialogView.findViewById(R.id.dialog_edit);

                    editText.setHint(R.string.dialog_title_hint);
                    editText.setText(title);

                    Button action_ok = dialogView.findViewById(R.id.action_ok);
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = editText.getText().toString().trim();
                            if (text.isEmpty()) {
                                NinjaToast.show(context, getString(R.string.toast_input_empty));
                            } else {
                                RecordAction action = new RecordAction(context);
                                action.open(true);
                                gridItem.setTitle(text);
                                action.updateGridItem(gridItem);
                                action.close();
                                hideKeyboard(activity);
                                open_favorite.performClick();
                            }
                            hideBottomSheetDialog ();
                        }
                    });
                    Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                    action_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideKeyboard(activity);
                            hideBottomSheetDialog ();
                        }
                    });
                    bottomSheetDialog.setContentView(dialogView);
                    bottomSheetDialog.show();
                    HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                } else if (CollectionTab.equals(getString(R.string.album_title_bookmarks))){
                    try {

                        bottomSheetDialog = new BottomSheetDialog(context);

                        View dialogView = View.inflate(context, R.layout.dialog_edit_bookmark, null);

                        final EditText pass_titleET = dialogView.findViewById(R.id.pass_title);
                        final EditText pass_userNameET = dialogView.findViewById(R.id.pass_userName);
                        final EditText pass_userPWET = dialogView.findViewById(R.id.pass_userPW);
                        final EditText pass_URLET = dialogView.findViewById(R.id.pass_url);
                        final ImageView ib_icon = dialogView.findViewById(R.id.ib_icon);

                        final String decrypted_userName = mahEncryptor.decode(userName);
                        final String decrypted_userPW = mahEncryptor.decode(userPW);

                        pass_titleET.setText(title);
                        pass_userNameET.setText(decrypted_userName);
                        pass_userPWET.setText(decrypted_userPW);
                        pass_URLET.setText(url);

                        Button action_ok = dialogView.findViewById(R.id.action_ok);
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    String input_pass_title = pass_titleET.getText().toString().trim();
                                    String input_pass_url = pass_URLET.getText().toString().trim();

                                    String encrypted_userName = mahEncryptor.encode(pass_userNameET.getText().toString().trim());
                                    String encrypted_userPW = mahEncryptor.encode(pass_userPWET.getText().toString().trim());

                                    db.update(Integer.parseInt(_id), HelperUnit.secString(input_pass_title), HelperUnit.secString(input_pass_url),  HelperUnit.secString(encrypted_userName), HelperUnit.secString(encrypted_userPW), pass_creation);
                                    initBookmarkList();
                                    hideKeyboard(activity);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    NinjaToast.show(context, R.string.toast_error);
                                }
                                hideBottomSheetDialog ();
                            }
                        });
                        Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                        action_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideKeyboard(activity);
                                hideBottomSheetDialog ();
                            }
                        });
                        HelperUnit.switchIcon(activity, pass_creation, "pass_creation", ib_icon);
                        bottomSheetDialog.setContentView(dialogView);
                        bottomSheetDialog.show();
                        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);

                        ib_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    final String input_pass_title = pass_titleET.getText().toString().trim();
                                    final String input_pass_url = pass_URLET.getText().toString().trim();
                                    final String encrypted_userName = mahEncryptor.encode(pass_userNameET.getText().toString().trim());
                                    final String encrypted_userPW = mahEncryptor.encode(pass_userPWET.getText().toString().trim());

                                    hideBottomSheetDialog ();
                                    hideKeyboard(activity);

                                    bottomSheetDialog = new BottomSheetDialog(context);
                                    View dialogView = View.inflate(context, R.layout.dialog_edit_icon, null);

                                    GridView grid = dialogView.findViewById(R.id.grid_filter);
                                    GridItem_filter itemAlbum_01 = new GridItem_filter(sp.getString("icon_01", getResources().getString(R.string.color_red)), "icon_01", getResources().getDrawable(R.drawable.circle_red_big), "01");
                                    GridItem_filter itemAlbum_02 = new GridItem_filter(sp.getString("icon_02", getResources().getString(R.string.color_pink)), "icon_02", getResources().getDrawable(R.drawable.circle_pink_big), "02");
                                    GridItem_filter itemAlbum_03 = new GridItem_filter(sp.getString("icon_03", getResources().getString(R.string.color_purple)), "icon_03", getResources().getDrawable(R.drawable.circle_purple_big), "03");
                                    GridItem_filter itemAlbum_04 = new GridItem_filter(sp.getString("icon_04", getResources().getString(R.string.color_blue)), "icon_04", getResources().getDrawable(R.drawable.circle_blue_big), "04");
                                    GridItem_filter itemAlbum_05 = new GridItem_filter(sp.getString("icon_05", getResources().getString(R.string.color_teal)), "icon_05", getResources().getDrawable(R.drawable.circle_teal_big), "05");
                                    GridItem_filter itemAlbum_06 = new GridItem_filter(sp.getString("icon_06", getResources().getString(R.string.color_green)), "icon_06", getResources().getDrawable(R.drawable.circle_green_big), "06");
                                    GridItem_filter itemAlbum_07 = new GridItem_filter(sp.getString("icon_07", getResources().getString(R.string.color_lime)), "icon_07", getResources().getDrawable(R.drawable.circle_lime_big), "07");
                                    GridItem_filter itemAlbum_08 = new GridItem_filter(sp.getString("icon_08", getResources().getString(R.string.color_yellow)), "icon_08", getResources().getDrawable(R.drawable.circle_yellow_big), "08");
                                    GridItem_filter itemAlbum_09 = new GridItem_filter(sp.getString("icon_09", getResources().getString(R.string.color_orange)), "icon_09", getResources().getDrawable(R.drawable.circle_orange_big), "09");
                                    GridItem_filter itemAlbum_10 = new GridItem_filter(sp.getString("icon_10", getResources().getString(R.string.color_brown)), "icon_10", getResources().getDrawable(R.drawable.circle_brown_big), "10");
                                    GridItem_filter itemAlbum_11 = new GridItem_filter(sp.getString("icon_11", getResources().getString(R.string.color_grey)), "icon_11", getResources().getDrawable(R.drawable.circle_grey_big), "11");

                                    final List<GridItem_filter> gridList = new LinkedList<>();
                                    if (sp.getBoolean("filter_01", true)){
                                        gridList.add(gridList.size(), itemAlbum_01);
                                    }
                                    if (sp.getBoolean("filter_02", true)){
                                        gridList.add(gridList.size(), itemAlbum_02);
                                    }
                                    if (sp.getBoolean("filter_03", true)){
                                        gridList.add(gridList.size(), itemAlbum_03);
                                    }
                                    if (sp.getBoolean("filter_04", true)){
                                        gridList.add(gridList.size(), itemAlbum_04);
                                    }
                                    if (sp.getBoolean("filter_05", true)){
                                        gridList.add(gridList.size(), itemAlbum_05);
                                    }
                                    if (sp.getBoolean("filter_06", true)){
                                        gridList.add(gridList.size(), itemAlbum_06);
                                    }
                                    if (sp.getBoolean("filter_07", true)){
                                        gridList.add(gridList.size(), itemAlbum_07);
                                    }
                                    if (sp.getBoolean("filter_08", true)){
                                        gridList.add(gridList.size(), itemAlbum_08);
                                    }
                                    if (sp.getBoolean("filter_09", true)){
                                        gridList.add(gridList.size(), itemAlbum_09);
                                    }
                                    if (sp.getBoolean("filter_10", true)){
                                        gridList.add(gridList.size(), itemAlbum_10);
                                    }
                                    if (sp.getBoolean("filter_11", true)){
                                        gridList.add(gridList.size(), itemAlbum_11);
                                    }
                                    GridAdapter_filter gridAdapter = new de.baumann.browser.view.GridAdapter_filter(context, gridList);
                                    grid.setAdapter(gridAdapter);
                                    gridAdapter.notifyDataSetChanged();

                                    grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            db.update(Integer.parseInt(_id), HelperUnit.secString(input_pass_title), HelperUnit.secString(input_pass_url),  HelperUnit.secString(encrypted_userName), HelperUnit.secString(encrypted_userPW), gridList.get(position).getOrdinal());
                                            initBookmarkList();
                                            hideBottomSheetDialog ();
                                        }
                                    });

                                    bottomSheetDialog.setContentView(dialogView);
                                    bottomSheetDialog.show();
                                    HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    hideBottomSheetDialog ();
                                    NinjaToast.show(context, R.string.toast_error);
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        NinjaToast.show(context, R.string.toast_error);
                    }
                }
            }
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    private void show_dialogFilter() {
        hideBottomSheetDialog();

        open_bookmark.performClick();

        bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_edit_icon, null);

        GridView grid = dialogView.findViewById(R.id.grid_filter);
        GridItem_filter itemAlbum_01 = new GridItem_filter(sp.getString("icon_01", getResources().getString(R.string.color_red)), "icon_01", getResources().getDrawable(R.drawable.circle_red_big), "01");
        GridItem_filter itemAlbum_02 = new GridItem_filter(sp.getString("icon_02", getResources().getString(R.string.color_pink)), "icon_02", getResources().getDrawable(R.drawable.circle_pink_big), "02");
        GridItem_filter itemAlbum_03 = new GridItem_filter(sp.getString("icon_03", getResources().getString(R.string.color_purple)), "icon_03", getResources().getDrawable(R.drawable.circle_purple_big), "03");
        GridItem_filter itemAlbum_04 = new GridItem_filter(sp.getString("icon_04", getResources().getString(R.string.color_blue)), "icon_04", getResources().getDrawable(R.drawable.circle_blue_big), "04");
        GridItem_filter itemAlbum_05 = new GridItem_filter(sp.getString("icon_05", getResources().getString(R.string.color_teal)), "icon_05", getResources().getDrawable(R.drawable.circle_teal_big), "05");
        GridItem_filter itemAlbum_06 = new GridItem_filter(sp.getString("icon_06", getResources().getString(R.string.color_green)), "icon_06", getResources().getDrawable(R.drawable.circle_green_big), "06");
        GridItem_filter itemAlbum_07 = new GridItem_filter(sp.getString("icon_07", getResources().getString(R.string.color_lime)), "icon_07", getResources().getDrawable(R.drawable.circle_lime_big), "07");
        GridItem_filter itemAlbum_08 = new GridItem_filter(sp.getString("icon_08", getResources().getString(R.string.color_yellow)), "icon_08", getResources().getDrawable(R.drawable.circle_yellow_big), "08");
        GridItem_filter itemAlbum_09 = new GridItem_filter(sp.getString("icon_09", getResources().getString(R.string.color_orange)), "icon_09", getResources().getDrawable(R.drawable.circle_orange_big), "09");
        GridItem_filter itemAlbum_10 = new GridItem_filter(sp.getString("icon_10", getResources().getString(R.string.color_brown)), "icon_10", getResources().getDrawable(R.drawable.circle_brown_big), "10");
        GridItem_filter itemAlbum_11 = new GridItem_filter(sp.getString("icon_11", getResources().getString(R.string.color_grey)), "icon_11", getResources().getDrawable(R.drawable.circle_grey_big), "11");

        final List<GridItem_filter> gridList = new LinkedList<>();
        if (sp.getBoolean("filter_01", true)){
            gridList.add(gridList.size(), itemAlbum_01);
        }
        if (sp.getBoolean("filter_02", true)){
            gridList.add(gridList.size(), itemAlbum_02);
        }
        if (sp.getBoolean("filter_03", true)){
            gridList.add(gridList.size(), itemAlbum_03);
        }
        if (sp.getBoolean("filter_04", true)){
            gridList.add(gridList.size(), itemAlbum_04);
        }
        if (sp.getBoolean("filter_05", true)){
            gridList.add(gridList.size(), itemAlbum_05);
        }
        if (sp.getBoolean("filter_06", true)){
            gridList.add(gridList.size(), itemAlbum_06);
        }
        if (sp.getBoolean("filter_07", true)){
            gridList.add(gridList.size(), itemAlbum_07);
        }
        if (sp.getBoolean("filter_08", true)){
            gridList.add(gridList.size(), itemAlbum_08);
        }
        if (sp.getBoolean("filter_09", true)){
            gridList.add(gridList.size(), itemAlbum_09);
        }
        if (sp.getBoolean("filter_10", true)){
            gridList.add(gridList.size(), itemAlbum_10);
        }
        if (sp.getBoolean("filter_11", true)){
            gridList.add(gridList.size(), itemAlbum_11);
        }

        GridAdapter_filter gridAdapter = new de.baumann.browser.view.GridAdapter_filter(context, gridList);
        grid.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sp.edit().putString("filter_bookmarks", gridList.get(position).getOrdinal()).apply();
                initBookmarkList();
                hideBottomSheetDialog ();
            }
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        HelperUnit.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    private void setCustomFullscreen(boolean fullscreen) {
        View decorView = getWindow().getDecorView();
        if (fullscreen) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    private AlbumController nextAlbumController(boolean next) {
        if (BrowserContainer.size() <= 1) {
            return currentAlbumController;
        }
        List<AlbumController> list = BrowserContainer.list();
        int index = list.indexOf(currentAlbumController);
        if (next) {
            index++;
            if (index >= list.size()) {
                index = 0;
            }
        } else {
            index--;
            if (index < 0) {
                index = list.size() - 1;
            }
        }
        return list.get(index);
    }

    public void setWintanUI(){
        //todo

        {//Show something in Wintan
            history_refresh.setVisibility(View.VISIBLE);
            bookmark_home.setVisibility(View.VISIBLE);
            TitleTv.setVisibility(View.VISIBLE);
        }
        {//Hide something in normal
            normalCenterLayout.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            forward.setVisibility(View.GONE);
        }

        isUIWintan=true;
        sp.edit().putBoolean(getString(R.string.sp_wintan_mode),isUIWintan).commit();
    }

    public void setNormalUI() {
        //todo

        {//Hide something in Wintan
            //wintan button
            history_refresh.setVisibility(View.GONE);
            bookmark_home.setVisibility(View.GONE);

            //Wintan title
            TitleTv.setVisibility(View.GONE);
        }
        {//Show something in normal
            normalCenterLayout.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
        }
        {   //Show something in normal
            if (ninjaWebView.canGoBack() || ninjaWebView.canGoForward()) {
                back.setVisibility(showIcon ? View.VISIBLE : View.GONE);
                forward.setVisibility(showIcon ? View.VISIBLE : View.GONE);
                back.setEnabled(!disableIcon && ninjaWebView.canGoBack());
                forward.setEnabled(!disableIcon && ninjaWebView.canGoForward());
            } else {
                back.setVisibility(View.GONE);
                forward.setVisibility(View.GONE);
            }

        }

        isUIWintan=false;
        sp.edit().putBoolean(getString(R.string.sp_wintan_mode),isUIWintan).commit();
    }

    public void goback()
    {
        if (urlbar.getVisibility() == View.VISIBLE) {
            hideUrlPanel();
        }else if (toolbar.getVisibility() == View.GONE && sp.getBoolean("sp_toolbarShow", true)) {
            showOmnibox();
        }else{
            if (ninjaWebView.canGoBack()) {
                ninjaWebView.goBack();
            } else {
                removeAlbum(currentAlbumController);
            }
        }
    }

}
