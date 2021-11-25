package hu.unideb.inf.mi.bsc.mobil.projekt.arvai.szabi.arducarcontroller;

public final class ConnectionData {

    private static ConnectionData INSTANCE;
    public boolean connected = false;
    BluetoothConnectionService myBtService;

    private ConnectionData() {
    }

    public static ConnectionData getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ConnectionData();
        }

        return INSTANCE;
    }

}

