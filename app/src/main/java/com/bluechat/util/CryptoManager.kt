package com.bluechat.util

import android.util.Base64
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECParameterSpec
import org.bouncycastle.jce.spec.ECPrivateKeySpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import org.bouncycastle.math.ec.ECPoint
import java.math.BigInteger
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

@Suppress("unused")
class CryptoManager {

    companion object {
        private const val ALGORITHM = "EC"
        private const val CURVE_NAME = "secp256r1"
        private const val KEY_AGREEMENT_ALGORITHM = "ECDH"
        private const val SYMMETRIC_ALGORITHM = "AES/GCM/NoPadding"
        private const val TAG_LENGTH = 128
        private const val IV_LENGTH = 12
        private const val KEY_LENGTH = 256
        
        init {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    private val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME).apply {
        val ecSpec = ECNamedCurveTable.getParameterSpec(CURVE_NAME)
        val ecParameterSpec = ECParameterSpec(
            ecSpec.curve, ecSpec.g, ecSpec.n, ecSpec.h, ecSpec.seed
        )
        initialize(ecParameterSpec, SecureRandom())
    }

    fun generateKeyPair(): KeyPair {
        return keyPairGenerator.generateKeyPair()
    }

    fun generateSharedSecret(privateKey: PrivateKey, publicKey: PublicKey): ByteArray {
        val keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        keyAgreement.init(privateKey)
        keyAgreement.doPhase(publicKey, true)
        return keyAgreement.generateSecret()
    }

    fun encryptData(data: ByteArray, key: ByteArray): Pair<ByteArray, ByteArray> {
        val iv = ByteArray(IV_LENGTH).also {
            SecureRandom().nextBytes(it)
        }
        
        val secretKey = SecretKeySpec(key, 0, KEY_LENGTH / 8, "AES")
        val cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        val parameterSpec = GCMParameterSpec(TAG_LENGTH, iv)
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec)
        val encryptedData = cipher.doFinal(data)
        
        return Pair(encryptedData, iv)
    }

    fun decryptData(encryptedData: ByteArray, key: ByteArray, iv: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, 0, KEY_LENGTH / 8, "AES")
        val cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        val parameterSpec = GCMParameterSpec(TAG_LENGTH, iv)
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec)
        return cipher.doFinal(encryptedData)
    }

    fun publicKeyToString(publicKey: PublicKey): String {
        return Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
    }

    fun privateKeyToString(privateKey: PrivateKey): String {
        return Base64.encodeToString(privateKey.encoded, Base64.NO_WRAP)
    }

    fun stringToPublicKey(publicKeyString: String): PublicKey {
        val keyBytes = Base64.decode(publicKeyString, Base64.NO_WRAP)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        return keyFactory.generatePublic(keySpec)
    }

    fun stringToPrivateKey(privateKeyString: String): PrivateKey {
        val keyBytes = Base64.decode(privateKeyString, Base64.NO_WRAP)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME)
        return keyFactory.generatePrivate(keySpec)
    }

    fun generateAESKey(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(KEY_LENGTH)
        return keyGenerator.generateKey().encoded
    }
}
