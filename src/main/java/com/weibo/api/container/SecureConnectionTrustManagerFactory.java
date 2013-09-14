package main.java.com.weibo.api.container;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;

/**
 * 虚假的 {@link TrustManagerFactorySpi}。接受任何证书，哪怕是非法的。
 * User: Aiden S. Zouliu
 * Date: 9/14/13
 * Time: 1:40 PM
 */
public class SecureConnectionTrustManagerFactory extends TrustManagerFactorySpi {

    private static final TrustManager DUMMY_TRUST_MANAGER = new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            // 啥都接受，示例而已。
            // 在实际使用的时候，必须做正经事情。
            System.err.println(
                    "UNKNOWN CLIENT CERTIFICATE: " + chain[0].getSubjectDN());
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            // 啥都接受，示例而已。
            // 在实际使用的时候，必须做正经事情。
            System.err.println(
                    "UNKNOWN SERVER CERTIFICATE: " + chain[0].getSubjectDN());
        }
    };

    public static TrustManager[] getTrustManagers() {
        return new TrustManager[] { DUMMY_TRUST_MANAGER };
    }

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        return getTrustManagers();
    }

    @Override
    protected void engineInit(ManagerFactoryParameters spec) throws InvalidAlgorithmParameterException {
        // 空实现。
    }

    @Override
    protected void engineInit(KeyStore ks) throws KeyStoreException {
        // 空实现。
    }
}
