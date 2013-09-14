package main.java.com.weibo.api.container;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.security.Security;

/**
 * 创建一个虚假的 {@link SSLContext}。
 * 该工厂类生成的客户端的 context 接受任意的证书，哪怕该证书是无效的。
 * 该工厂类生成的服务端的 context 会发送一个虚假的证书。
 * <p>在实际使用中，需要按照需求创建不同的 contexts。</p>
 * <h3>客户端证书认证</h3>
 * <ul>
 *     <li>在创建 {@link io.netty.handler.ssl.SslHandler} 之前在服务端调用 {@link javax.net.ssl.SSLEngine#setNeedClientAuth(boolean)}。</li>
 *     <li>在客户端初始化 {@link SSLContext} 时，指定包含客户端证书的 {@link javax.net.ssl.KeyManager} 作为 {@link SSLContext#init(javax.net.ssl.KeyManager[], javax.net.ssl.TrustManager[], java.security.SecureRandom)} 的第一个参数。</li>
 *     <li>在服务端初始化 {@link SSLContext} 时，指定合适的 {@link javax.net.ssl.TrustManager} 作为 {@link SSLContext#init(javax.net.ssl.KeyManager[], javax.net.ssl.TrustManager[], java.security.SecureRandom)} 的第二个参数，用来验证证书。</li>
 * </ul>
 * User: Aiden S. Zouliu
 * Date: 9/14/13
 * Time: 1:13 PM
 */
public final class SecureConnectionSslContextFactory {

    private static final String PROTOCOL = "TLS";
    private static final SSLContext SERVER_CONTEXT;
    private static final SSLContext CLIENT_CONTEXT;

    static {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }

        SSLContext serverContext;
        SSLContext clientContext;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(SecureConnectionKeyStore.asInputStream(),
                    SecureConnectionKeyStore.getKeyStorePassword());

            // 创建 KeyManagerFactory 以使用生成的 KeyStore。
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, SecureConnectionKeyStore.getCertificatePassword());

            // 初始化 SSLContext 配合 KeyManagerFactory 使用。
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), null, null);
        } catch (final Exception e) {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }

        try {
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(null, SecureConnectionTrustManagerFactory.getTrustManagers(), null);
        } catch (Exception e) {
            throw new Error("Failed to initialize the client-side SSLContext", e);
        }

        SERVER_CONTEXT = serverContext;
        CLIENT_CONTEXT = clientContext;
    }

    /**
     * 返回服务器端的 {@link SSLContext}。
     */
    public static SSLContext getServerContext() {
        return SERVER_CONTEXT;
    }

    /**
     * 返回客户端的 {@link SSLContext}。
     */
    public static SSLContext getClientContext() {
        return CLIENT_CONTEXT;
    }

    private SecureConnectionSslContextFactory() {
        // 空实现。
    }

}
