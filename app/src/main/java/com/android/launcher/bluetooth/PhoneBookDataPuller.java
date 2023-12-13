package com.android.launcher.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.bluetooth.client.pbap.BluetoothPbapClient;
import com.android.vcard.VCardEntry;

import module.common.MessageEvent;
import com.android.launcher.util.FuncUtil;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;
import module.common.utils.StringUtils;

import com.bzf.module_db.entity.ContactsTable;
import com.bzf.module_db.repository.ContactsRepository;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @description: 蓝牙电话本拉取
 * @createDate: 2023/5/24
 */
public class PhoneBookDataPuller {
    private static final String TAG = PhoneBookDataPuller.class.getSimpleName();

    private BluetoothPbapClient client = null;
    private BluetoothDevice device = null;

    private Context context;

    private BluetoothServiceHandler handler = new BluetoothServiceHandler();

    private boolean isLoading = false;

    public PhoneBookDataPuller(Context context) {
        this.context = context;
    }

    public void getData(BluetoothDevice connectDevice) {
        try {
            if (connectDevice != null) {
                this.device = connectDevice;
                client = new BluetoothPbapClient(device, handler);
                client.connect();
                boolean pullPhoneBook = client.pullPhoneBook(BluetoothPbapClient.PB_PATH, 0, BluetoothPbapClient.VCARD_TYPE_30, 0, 0);
                if (pullPhoneBook){
                    isLoading = true;
                }
                String macAddress = device.getAddress().replaceAll(":", "");

                SPUtils.putString(context, SPUtils.SP_BLUETOOTH_DEVICEID, macAddress);
                LogUtils.printI(TAG, "pullPhoneBook=" + pullPhoneBook);
            } else {
                LogUtils.printI(TAG, "当前没有蓝牙设备连接");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 获取连接的蓝牙设备
     * @createDate: 2023/5/29
     */
    public BluetoothDevice getConnectDevice() {
        BluetoothDevice connectDevice = null;
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
            LogUtils.printI(TAG, "getConnectDevice---pairedDevices="+pairedDevices.size());
            connectDevice = null;
            if (!pairedDevices.isEmpty()) {
                Iterator<BluetoothDevice> deviceIterator = pairedDevices.iterator();

                while (deviceIterator.hasNext()) {
                    BluetoothDevice bluetoothDevice = deviceIterator.next();
                    if(bluetoothDevice!=null){
                        LogUtils.printI(TAG, "device=" + bluetoothDevice.getName() + ", address=" + bluetoothDevice.getAddress() + ", bondState=" + bluetoothDevice.getBondState());
                        if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                            connectDevice = bluetoothDevice;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(connectDevice == null){
            LogUtils.printI(TAG, "没有获取到配对的蓝牙设备");
        }
        return connectDevice;
    }

    public class BluetoothServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            LogUtils.printI(TAG, "msg=" + msg.what);
            switch (msg.what) {
                case BluetoothPbapClient.EVENT_PULL_PHONE_BOOK_DONE: {
                    try {
                        if (msg.obj != null) {
                            LogUtils.printI(TAG, "msg=" + msg.obj);
                            ArrayList<VCardEntry> vCardEntrys = (ArrayList<VCardEntry>) msg.obj;
                            new Thread(() -> {
                                try {
                                    disposePhoneBookData(vCardEntrys);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                release();
                            }).start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case BluetoothPbapClient.EVENT_PULL_PHONE_BOOK_ERROR:
                    //下载电话本出错
                    LogUtils.printI(TAG, "下载电话本出错");
                    release();
                case BluetoothPbapClient.EVENT_SESSION_AUTH_TIMEOUT:
                    LogUtils.printI(TAG, "授权超时, 未能在规定的时间内对连接进行授权或拒绝，从而导致会话无法正常建立");
                    release();
//                    BluetoothDeviceHelper.deleteDevice(device);
                    break;
                case BluetoothPbapClient.EVENT_SESSION_DISCONNECTED:
                    LogUtils.printI(TAG, "连接断开");
                    release();
                    break;
                default: {

                }
            }
        }

        private synchronized void disposePhoneBookData(ArrayList<VCardEntry> vCardEntrys) throws Exception{
            if (vCardEntrys != null && !vCardEntrys.isEmpty()) {
                List<ContactsTable> contactsTableList = new ArrayList<>();
                if (device == null) {
                    return;
                }
                String macAddress = device.getAddress().replaceAll(":", "");
                for (int i = 0; i < vCardEntrys.size(); i++) {
                    VCardEntry vCardEntry = vCardEntrys.get(i);
                    if (vCardEntry != null) {
                        ContactsTable contactsTable = new ContactsTable();
                        int hashCode = vCardEntry.hashCode();
                        contactsTable.setId(macAddress + hashCode);

                        contactsTable.setDeviceId(macAddress);

                        VCardEntry.NameData nameData = vCardEntry.getNameData();
                        String family = StringUtils.removeNull(nameData.getFamily());
                        String given = StringUtils.removeNull(nameData.getGiven());
                        contactsTable.setName(family + given);

                        List<VCardEntry.PhoneData> phoneList = vCardEntry.getPhoneList();
                        if (phoneList != null && phoneList.size() > 0) {

                            VCardEntry.PhoneData phoneData = phoneList.get(0);
                            if (phoneData != null) {
                                String phone1 = phoneData.getNumber();
                                if (!TextUtils.isEmpty(phone1) && !phone1.equalsIgnoreCase("null")) {
                                    contactsTable.setPhone1(phone1.replaceAll("-", ""));
                                }
                            }
                            if (phoneList.size() > 1) {
                                VCardEntry.PhoneData phoneData1 = phoneList.get(1);
                                String phone2 = phoneData1.getNumber();
                                if (!TextUtils.isEmpty(phone2) && !phone2.equalsIgnoreCase("null")) {
                                    contactsTable.setPhone1(phone2.replaceAll("-", ""));
                                }
                            }
                            if (phoneList.size() > 2) {
                                VCardEntry.PhoneData phoneData2 = phoneList.get(2);
                                String phone3 = phoneData2.getNumber();
                                if (!TextUtils.isEmpty(phone3) && !phone3.equalsIgnoreCase("null")) {
                                    contactsTable.setPhone1(phone3.replaceAll("-", ""));
                                }
                            }
                        }

                        if (!TextUtils.isEmpty(contactsTable.getPhone1()) || !TextUtils.isEmpty(contactsTable.getPhone2()) || !TextUtils.isEmpty(contactsTable.getPhone3())) {
                            contactsTableList.add(contactsTable);
                        }
                    }
                }
                ContactsRepository.getInstance().saveData(context, macAddress, contactsTableList);
                LogUtils.printI(TAG, "电话本数据读取完成");
            } else {
                LogUtils.printI(TAG, "电话本没数据");
            }
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.CONTACTS_PULL_FINISH));
        }
    }

    private void release() {
        try {
            isLoading = false;
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isLoading() {
        return isLoading;
    }
}