package com.vibes.ux.server.repository;

import com.vibes.ux.server.AppProperties;
import com.vibes.ux.server.CryptoService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Properties;

@Component
@Slf4j
public class ServerRepository {

    private final AppProperties appProperties;

    private ECPublicKey publicKey;

    private byte[] publicKeyUncompressed;

    private String publicKeyBase64;

    private ECPrivateKey privateKey;

    private final CryptoService cryptoService;

    public ServerRepository(AppProperties appProperties, CryptoService cryptoService) {
        this.appProperties = appProperties;
        this.cryptoService = cryptoService;
    }

    public byte[] getPublicKeyUncompressed() {
        return this.publicKeyUncompressed;
    }

    public String getPublicKeyBase64() {
        return this.publicKeyBase64;
    }

    public ECPrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public ECPublicKey getPublicKey() {
        return this.publicKey;
    }



    @PostConstruct
    public void init() {
        Path appServerPublicKeyFile = Paths.get(this.appProperties.getServerPublicKeyPath());
        Path appServerPrivateKeyFile = Paths.get(this.appProperties.getServerPrivateKeyPath());

        if (Files.exists(appServerPublicKeyFile) && Files.exists(appServerPrivateKeyFile)) {
            loadKeyPair(appServerPublicKeyFile, appServerPrivateKeyFile);
        } else {
            generateKeyPair(appServerPublicKeyFile, appServerPrivateKeyFile);
        }
    }

    private void generateKeyPair(Path publicPath, Path privatePath) {
        try {
            KeyPair pair = this.cryptoService.getKeyPairGenerator().generateKeyPair();
            this.publicKey = (ECPublicKey) pair.getPublic();
            this.privateKey = (ECPrivateKey) pair.getPrivate();

            Files.write(publicPath, this.publicKey.getEncoded());
            Files.write(privatePath, this.privateKey.getEncoded());

            this.publicKeyUncompressed = CryptoService.toUncompressedECPublicKey(this.publicKey);
            this.publicKeyBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(this.publicKeyUncompressed);
        } catch (IOException e) {
            log.error("Write files", e);
        }
    }

    private void loadKeyPair(Path publicPath, Path privatePath) {
        try {
            byte[] appServerPublicKey = Files.readAllBytes(publicPath);
            byte[] appServerPrivateKey = Files.readAllBytes(privatePath);

            this.publicKey = (ECPublicKey) this.cryptoService.convertX509ToECPublicKey(appServerPublicKey);
            this.privateKey = (ECPrivateKey) this.cryptoService.convertPKCS8ToECPrivateKey(appServerPrivateKey);

            this.publicKeyUncompressed = CryptoService.toUncompressedECPublicKey(this.publicKey);

            this.publicKeyBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(this.publicKeyUncompressed);
        } catch (InvalidKeySpecException | IOException e) {
            log.error("Load files", e);
        }
    }
}
