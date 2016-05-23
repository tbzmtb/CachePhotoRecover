package sunyung.kr.photoCacheRecover;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DataHandler mDataHandler;
    private String TAG = getClass().getName();
    private GridView mGridView;
    private ImageGridViewAdapter mAdpater;
    private Button mBtnRepair;
    private ArrayList<GridViewData> mPathArray = new ArrayList<>();
    private IInAppBillingService mService;
    private IabHelper mHelper;
    private String[] billingData = {"photo_0", "photo_1", "photo_2", "photo_3", "photo_4", "photo_5", "photo_6", "photo_7", "photo_8", "photo_9", "photo_10", "photo_11", "photo_12", "photo_13", "photo_14", "photo_15", "photo_16", "photo_17", "photo_18", "photo_19"};
    private int[] billingCountMax = {5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 10000};

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvdoifWcgI6CLXJPEhcsY8trszWWuhtpV2OpQSV2TssggMtWAj+Ndh+kpqHTc8FbzUwrq3rmHhPDTFAlZInZhnW8zwSE/eDhnMHI3pyWmpO/0RxEQOPJxcYqpFpYiAhcyc3dLUowAFTuyVx4uoCncyXKRSwyMBM3B+RFW6ntt6FUlwEg0b491Uchc2Blz4a2utOKbNj4XLcMfJrQHIR6yP1ljoQAHwM0bX1dos0afexCCzf6u5YlJi8ng4CQ56SRUbOTHGRhu/SA+8A0+tbQP7vr7HCb1xSfs7KL0N2pC6H9Gy1JY4bkVpvOxf6zFtTX3x9wa7Kb2RD2qhAh9k8HN7wIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Logger.d(TAG, "fail");
                    // 구매오류처리 ( 토스트하나 띄우고 결제팝업 종료시키면 되겠습니다 )
                }

                // 구매목록을 초기화하는 메서드입니다.
                // v3으로 넘어오면서 구매기록이 모두 남게 되는데 재구매 가능한 상품( 게임에서는 코인같은아이템은 ) 구매후 삭제해주어야 합니다.
                // 이 메서드는 상품 구매전 혹은 후에 반드시 호출해야합니다. ( 재구매가 불가능한 1회성 아이템의경우 호출하면 안됩니다 )
                AlreadyPurchaseItems();
            }
        });

        File file = new File(Config.NEW_IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        mDataHandler = new DataHandler();
        mGridView = (GridView) findViewById(R.id.grid_view);
        mAdpater = new ImageGridViewAdapter(MainActivity.this, mPathArray);
        mGridView.setAdapter(mAdpater);
        mBtnRepair = (Button) findViewById(R.id.btn_repair);
        mBtnRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdpater == null) {
                    return;
                }
                ArrayList<GridViewData> data = mAdpater.getSelectedItem();
                Logger.d(TAG, "data size = " + data.size());
                int count = data.size();
                if (count == 0) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.please_seleted_photo), Toast.LENGTH_SHORT).show();
                } else if (count > 10000) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.please_seleted_photo), Toast.LENGTH_SHORT).show();
                } else {
                    String item = getItemId(data);
                    Buy(item); // 제품id를 써줍니다. 앱배포시에 인앱상품등록시 등록할 id입니다.
                }

            }
        });
        new ScanFileDataTask(MainActivity.this, mDataHandler).execute();
    }

    public String getItemId(ArrayList<GridViewData> data) {
        int count = data.size();
        for (int i = 0; i < billingCountMax.length; i++) {
            if (count <= billingCountMax[i]) {
                return billingData[i];
            }
        }
        return null;
    }

    public void setButtonText(int num) {
        if (num == 0) {
            mBtnRepair.setText(getResources().getString(R.string.repaire));
        } else {
            mBtnRepair.setText(getResources().getString(R.string.repaire) + " " + num);
        }
    }

    public void AlreadyPurchaseItems() {
        try {
            Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
            int response = ownedItems.getInt("RESPONSE_CODE");
            if (response == 0) {

                ArrayList purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                String[] tokens = new String[purchaseDataList.size()];
                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = (String) purchaseDataList.get(i);
                    JSONObject jo = new JSONObject(purchaseData);
                    tokens[i] = jo.getString("purchaseToken");
                    // 여기서 tokens를 모두 컨슘 해주기
                    mService.consumePurchase(3, getPackageName(), tokens[i]);
                }
            }
            // 토큰을 모두 컨슘했으니 구매 메서드 처리
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 구매
    public void Buy(String id_item) {
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(), id_item, "inapp", "test");
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

            if (pendingIntent != null) {
                startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
//                mHelper.launchPurchaseFlow(this, getPackageName(), 1001, mPurchaseFinishedListener, "test");

            } else {
                // 결제가 막혔다면
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//            repairingPictures(mAdpater.getSelectedItem());
            // 여기서 아이템 추가 해주시면 됩니다.
            // 만약 서버로 영수증 체크후에 아이템 추가한다면, 서버로 purchase.getOriginalJson() , purchase.getSignature() 2개 보내시면 됩니다.
        }


    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceConn != null) {
            unbindService(mServiceConn);
        }
        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mHelper == null) {
            return; //iabHelper가 null값인경우 리턴
        }

        if (requestCode == 1001)
            if (resultCode == RESULT_OK) {
                if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
                    super.onActivityResult(requestCode, resultCode, data);

                    int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
                    repairingPictures(mAdpater.getSelectedItem());
                }
            }

    }

    public void repairingPictures(ArrayList<GridViewData> data) {
        new PictureRepairTask(MainActivity.this, data, mDataHandler).execute();
    }


    public class DataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Config.FILE_SCAN_HANDLER) {
                mPathArray.clear();
                ArrayList<GridViewData> data = (ArrayList<GridViewData>) msg.obj;
                mPathArray.addAll(data);
                mAdpater.notifyDataSetChanged();


            } else if (msg.what == Config.FILE_REPAIR_HANDLER) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.Success), Toast.LENGTH_LONG).show();
                mAdpater.setAllDataDeSeleted();
                mAdpater.notifyDataSetChanged();
            }
        }
    }
}

