package org.forgerock.auth.node.bankid.client.env;

import org.forgerock.auth.node.bankid.BankIDNode;
import org.forgerock.openam.utils.StringUtils;

import java.net.URI;

public class BankIDEnvironment {

    private static final String testCA = "-----BEGIN CERTIFICATE-----\n" +
            "MIIF0DCCA7igAwIBAgIIIhYaxu4khgAwDQYJKoZIhvcNAQENBQAwbDEkMCIGA1UE\n" +
            "CgwbRmluYW5zaWVsbCBJRC1UZWtuaWsgQklEIEFCMRowGAYDVQQLDBFJbmZyYXN0\n" +
            "cnVjdHVyZSBDQTEoMCYGA1UEAwwfVGVzdCBCYW5rSUQgU1NMIFJvb3QgQ0EgdjEg\n" +
            "VGVzdDAeFw0xNDExMjExMjM5MzFaFw0zNDEyMzExMjM5MzFaMGwxJDAiBgNVBAoM\n" +
            "G0ZpbmFuc2llbGwgSUQtVGVrbmlrIEJJRCBBQjEaMBgGA1UECwwRSW5mcmFzdHJ1\n" +
            "Y3R1cmUgQ0ExKDAmBgNVBAMMH1Rlc3QgQmFua0lEIFNTTCBSb290IENBIHYxIFRl\n" +
            "c3QwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCAKWsJc/kV/0434d+S\n" +
            "qn19mIr85RZ/PgRFaUplSrnhuzAmaXihPLCEsd3Mh/YErygcxhQ/MAzi5OZ/anfu\n" +
            "WSCwceRlQINtvlRPdMoeZtu29FsntK1Z5r2SYNdFwbRFb8WN9FsU0KvC5zVnuDMg\n" +
            "s5dUZwTmdzX5ZdLP7pdgB3zhTnra5ORtkiWiUxJVev9keRgAo00ZHIRJ+xTfiSPd\n" +
            "Jc314maigVRQZdGKSyQcQMTWi1YLwd2zwOacNxleYf8xqKgkZsmkrc4Dp2mR5Pkr\n" +
            "nnKB6A7sAOSNatua7M86EgcGi9AaEyaRMkYJImbBfzaNlaBPyMSvwmBZzp2xKc9O\n" +
            "D3U06ogV6CJjJL7hSuVc5x/2H04d+2I+DKwep6YBoVL9L81gRYRycqg+w+cTZ1TF\n" +
            "/s6NC5YRKSeOCrLw3ombhjyyuPl8T/h9cpXt6m3y2xIVLYVzeDhaql3hdi6IpRh6\n" +
            "rwkMhJ/XmOpbDinXb1fWdFOyQwqsXQWOEwKBYIkM6cPnuid7qwaxfP22hDgAolGM\n" +
            "LY7TPKUPRwV+a5Y3VPl7h0YSK7lDyckTJdtBqI6d4PWQLnHakUgRQy69nZhGRtUt\n" +
            "PMSJ7I4Qtt3B6AwDq+SJTggwtJQHeid0jPki6pouenhPQ6dZT532x16XD+WIcD2f\n" +
            "//XzzOueS29KB7lt/wH5K6EuxwIDAQABo3YwdDAdBgNVHQ4EFgQUDY6XJ/FIRFX3\n" +
            "dB4Wep3RVM84RXowDwYDVR0TAQH/BAUwAwEB/zAfBgNVHSMEGDAWgBQNjpcn8UhE\n" +
            "Vfd0HhZ6ndFUzzhFejARBgNVHSAECjAIMAYGBCoDBAUwDgYDVR0PAQH/BAQDAgEG\n" +
            "MA0GCSqGSIb3DQEBDQUAA4ICAQA5s59/Olio4svHXiKu7sPQRvrf4GfGB7hUjBGk\n" +
            "YW2YOHTYnHavSqlBASHc8gGGwuc7v7+H+vmOfSLZfGDqxnBqeJx1H5E0YqEXtNqW\n" +
            "G1JusIFa9xWypcONjg9v7IMnxxQzLYws4YwgPychpMzWY6B5hZsjUyKgB+1igxnf\n" +
            "uaBueLPw3ZaJhcCL8gz6SdCKmQpX4VaAadS0vdMrBOmd826H+aDGZek1vMjuH11F\n" +
            "fJoXY2jyDnlol7Z4BfHc011toWNMxojI7w+U4KKCbSxpWFVYITZ8WlYHcj+b2A1+\n" +
            "dFQZFzQN+Y1Wx3VIUqSks6P7F5aF/l4RBngy08zkP7iLA/C7rm61xWxTmpj3p6SG\n" +
            "fUBsrsBvBgfJQHD/Mx8U3iQCa0Vj1XPogE/PXQQq2vyWiAP662hD6og1/om3l1PJ\n" +
            "TBUyYXxqJO75ux8IWblUwAjsmTlF/Pcj8QbcMPXLMTgNQAgarV6guchjivYqb6Zr\n" +
            "hq+Nh3JrF0HYQuMgExQ6VX8T56saOEtmlp6LSQi4HvKatCNfWUJGoYeT5SrcJ6sn\n" +
            "By7XLMhQUCOXcBwKbNvX6aP79VA3yeJHZO7XParX7V9BB+jtf4tz/usmAT/+qXtH\n" +
            "CCv9Xf4lv8jgdOnFfXbXuT8I4gz8uq8ElBlpbJntO6p/NY5a08E6C7FWVR+WJ5vZ\n" +
            "OP2HsA==\n" +
            "-----END CERTIFICATE-----";

    private static final String prodCA = "-----BEGIN CERTIFICATE-----\n" +
            "MIIFvjCCA6agAwIBAgIITyTh/u1bExowDQYJKoZIhvcNAQENBQAwYjEkMCIGA1UE\n" +
            "CgwbRmluYW5zaWVsbCBJRC1UZWtuaWsgQklEIEFCMRowGAYDVQQLDBFJbmZyYXN0\n" +
            "cnVjdHVyZSBDQTEeMBwGA1UEAwwVQmFua0lEIFNTTCBSb290IENBIHYxMB4XDTEx\n" +
            "MTIwNzEyMzQwN1oXDTM0MTIzMTEyMzQwN1owYjEkMCIGA1UECgwbRmluYW5zaWVs\n" +
            "bCBJRC1UZWtuaWsgQklEIEFCMRowGAYDVQQLDBFJbmZyYXN0cnVjdHVyZSBDQTEe\n" +
            "MBwGA1UEAwwVQmFua0lEIFNTTCBSb290IENBIHYxMIICIjANBgkqhkiG9w0BAQEF\n" +
            "AAOCAg8AMIICCgKCAgEAwVA4snZiSFI3r64LvYu4mOsI42A9aLKEQGq4IZo257iq\n" +
            "vPH82SMvgBJgE52kCx7gQMmZ7iSm39CEA19hlILh8JEJNTyJNxMxVDN6cfJP1jMH\n" +
            "JeTES1TmVbWUqGyLpyT8LCJhC9Vq4W3t/O1svGJNOUQIQL4eAHSvWTVoalxzomJh\n" +
            "On97ENjXAt4BLb6sHfVBvmB5ReK0UfwpNACFM1RN8btEaDdWC4PfA72yzV3wK/cY\n" +
            "5h2k1RM1s19PjoxnpJqrmn4qZmP4tN/nk2d7c4FErJAP0pnNsll1+JfkdMfiPD35\n" +
            "+qcclpspzP2LpauQVyPbO21Nh+EPtr7+Iic2tkgz0g1kK0IL/foFrJ0Ievyr3Drm\n" +
            "2uRnA0esZ45GOmZhE22mycEX9l7w9jrdsKtqs7N/T46hil4xBiGblXkqKNG6TvAR\n" +
            "k6XqOp3RtUvGGaKZnGllsgTvP38/nrSMlszNojrlbDnm16GGoRTQnwr8l+Yvbz/e\n" +
            "v/e6wVFDjb52ZB0Z/KTfjXOl5cAJ7OCbODMWf8Na56OTlIkrk5NyU/uGzJFUQSvG\n" +
            "dLHUipJ/sTZCbqNSZUwboI0oQNO/Ygez2J6zgWXGpDWiN4LGLDmBhB3T8CMQu9J/\n" +
            "BcFvgjnUyhyim35kDpjVPC8nrSir5OkaYgGdYWdDuv1456lFNPNNQcdZdt5fcmMC\n" +
            "AwEAAaN4MHYwHQYDVR0OBBYEFPgqsux5RtcrIhAVeuLBSgBuRDFVMA8GA1UdEwEB\n" +
            "/wQFMAMBAf8wHwYDVR0jBBgwFoAU+Cqy7HlG1ysiEBV64sFKAG5EMVUwEwYDVR0g\n" +
            "BAwwCjAIBgYqhXBOAQQwDgYDVR0PAQH/BAQDAgEGMA0GCSqGSIb3DQEBDQUAA4IC\n" +
            "AQAJOjUOS2GJPNrrrqf539aN1/EbUj5ZVRjG4wzVtX5yVqPGcRZjUQlNTcfOpwPo\n" +
            "czKBnNX2OMF+Qm94bb+xXc/08AERqJJ3FPKu8oDNeK+Rv1X4nh95J4RHZcvl4AGh\n" +
            "ECmGMyhyCea0qZBFBsBqQR7oC9afYOxsSovaPqX31QMLULWUYoBKWWHLVVIoHjAm\n" +
            "GtAzMkLwe0/lrVyApr9iyXWhVr+qYGmFGw1+rwmvDmmSLWNWawYgH4NYxTf8z5hB\n" +
            "iDOdAgilvyiAF8Yl0kCKUB2fAPhRNYlEcN+UP/KL24h/pB+hZ9mvR0tM6nW3HVZa\n" +
            "DrvRz4VihZ8vRi3fYnOAkNE6kZdrrdO7LdBc9yYkfQdTcy0N+Aw7q4TkQ8npomrV\n" +
            "mTKaPhtGhA7VICyRNBVcvyoxr+CY7aRQyHn/C7n/jRsQYxs7uc+msq6jRS4HPK8o\n" +
            "lnF9usWZX6KY+8mweJiTE4uN4ZUUBUtt8WcXXDiK/bxEG2amjPcZ/b4LXwGCJb+a\n" +
            "NWP4+iY6kBKrMANs01pLvtVjUS9RtRrY3cNEOhmKhO0qJSDXhsTcVtpbDr37UTSq\n" +
            "QVw83dReiARPwGdURmmkaheH6z4k6qEUSXuFch0w53UAc+1aBXR1bgyFqMdy7Yxi\n" +
            "b2AYu7wnrHioDWqP6DTkUSUeMB/zqWPM/qx6QNNOcaOcjA==\n" +
            "-----END CERTIFICATE-----";

    public static final String testKeyPairSecret = "Bag Attributes\n" +
            "    localKeyID: 01 00 00 00 \n" +
            "    friendlyName: {557681F5-FDF4-4AA2-AC94-E4591DCB02D5}\n" +
            "    Microsoft CSP Name: Microsoft Enhanced Cryptographic Provider v1.0\n" +
            "Key Attributes\n" +
            "    X509v3 Key Usage: 10 \n" +
            "-----BEGIN ENCRYPTED PRIVATE KEY-----\n" +
            "MIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQI3kj2Me1cpw8CAggA\n" +
            "MBQGCCqGSIb3DQMHBAiIPevtzWadjQSCBMhfi1VSmSMOlED+KLbQVLdz73R9v0EA\n" +
            "3Kq0GqRu2t3WrpnCXkZP+QO8hr/MfbmUKsp0OeLZbRmD/bq84plGDOZTvtMHrgKU\n" +
            "QyiKKE80EUGSkJkEIXyOtHdBJyjOT+K2up/qZ8NQARjDFgUzC1vddy7U5YVL9IOm\n" +
            "h8h71BFD693rTFh8rzrpmHi2K193BKDwpnGDM6OlMvKOz6JwWbE3gQRm+kUkp19N\n" +
            "4Jj5o8UdJPo9EAinvNGmCN/R+b2mAG599R504d/gMJTSt7ibvdKPvIArnlxFONF+\n" +
            "oPhZjQHKT7cyvwfaTUZ3oicZo/ns1bhgwPd4zqhZEI8gYF96Pdm5wRlOfdNj+689\n" +
            "U74TzU6q6zaFK78vGHiA2NPxVwvPI/ZN4mhp8H7oDttHkAXa+ji6zGv1Uyg5X0WD\n" +
            "ZPsy8p/veCX2sT0XSNcGSOYTr4XA1vYtY+v3itwNMRXZLopVVnOYmvyd+JlBAb1A\n" +
            "6G2N+lH8DoNLyUj61Lq0UG5oFmyt4WBrLi+C8V34ZqlRJZSdXmC37oE7i1z2EXdW\n" +
            "l6N3dKpyRmZ2yqhx6gfcVreC8mXAim5k5ra+AMY3CiNyLKT2sLt+3ZwZPkW3i8K0\n" +
            "QXd/FEoKwOetRU9d8pYwvFEWsAV3HCpPEf4iMu+4G0+2nOmym1fyICAQ4TtGFPP8\n" +
            "0cvOnO23qkR6WwXE/13+5wO0tcl3o/VjvhnrVUgNpUlhuTKXgqgPYG90PTNl402e\n" +
            "J+k/P5BRjCXMdSlxIlLvcpnAUc1mHHg3kLAwTb2Oio/e0B7VG35mo4KyxtsLBXwQ\n" +
            "zi8JFbLGAtoh8tDI6VuLE+dqY6ijBGRvdlcf7pcb9x1/Fa5nWCVag5PXEYLXr6ap\n" +
            "oKHPH63as6tbV8POGtwJ6zPNfhr5Un4GLmMGofBiJh8AqrwzKMPUuCdvxJkGX5SJ\n" +
            "+Wjrlm1QYyEsDzz1ss9YrkysZhlVlghN0jqSC7RyX8gPTXGFislhDDlafupQtst0\n" +
            "7Hz04pOhA4ZqJpSqGH26cgRQ5yupsLGFlqeG18wSr5A6wwHto/WGKn1vnXQf9/N1\n" +
            "zxVFIo4JR2UrvV6X1Y0f2e/aZtMVFDrd0KcmymfPVrlRoL074zYCzL5lchqWbF8T\n" +
            "GKrer20RrLyrJQPq3T/JYirLjUVScqblcobARGyd55qhUqoDer9Q3nV4zMsAyioN\n" +
            "VOXBXdz+bAgRCS9EtwzfpeanMT1ezZvSAgRSauxEjTuxE0FML6HgOqad5/qUbtH+\n" +
            "CKEKP/bydtkpWqhKaV5dvnJnHhdBCb23XCzLh8phm2yrGGh3hxmocVP1Uj5EHGQk\n" +
            "iclo1GVt60aC3GRtnjtZqhKS2NLnqaJUPIypiDSEAzKK9duKP1SRm8M1htrPO56i\n" +
            "1kVEFgLJfUrAx8MoBZGO7JsNzPIfuS1Ib+Coodid9tZi9WHT4MmOWBHtnIqemNOK\n" +
            "rh1rLwicnplYRAZvLGiY0uquQ3/ne74lS8Dez9ODBAZDJx08jmrENJGfO6inq/ul\n" +
            "Warpgtwa/mYzy8derjikjHq2xSX83bsEn07QMIV01VURQTKElFfZiAIKQ/13eCE7\n" +
            "S8Eg4O0EfW7levD9Fb6XunecsBCLB1YlzydFfZ67fRidRiWpzzgtLZHNjAYvL/ns\n" +
            "GPM=\n" +
            "-----END ENCRYPTED PRIVATE KEY-----\n";
    public static final String testKeyPair = "Bag Attributes\n" +
            "    localKeyID: 01 00 00 00 \n" +
            "    friendlyName: FP Testcert 2\n" +
            "subject=/C=SE/O=Testbank A AB (publ)/serialNumber=12345678/name=Test av BankID/CN=FP Testcert 2\n" +
            "issuer=/C=SE/O=Testbank A AB (publ)/serialNumber=111111111111/CN=Testbank A RP CA v1 for BankID Test\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIEyDCCArCgAwIBAgIIQhrfHSgUmDEwDQYJKoZIhvcNAQELBQAwcTELMAkGA1UE\n" +
            "BhMCU0UxHTAbBgNVBAoMFFRlc3RiYW5rIEEgQUIgKHB1YmwpMRUwEwYDVQQFEwwx\n" +
            "MTExMTExMTExMTExLDAqBgNVBAMMI1Rlc3RiYW5rIEEgUlAgQ0EgdjEgZm9yIEJh\n" +
            "bmtJRCBUZXN0MB4XDTE1MDgxNzIyMDAwMFoXDTIwMDgxODIxNTk1OVowcDELMAkG\n" +
            "A1UEBhMCU0UxHTAbBgNVBAoMFFRlc3RiYW5rIEEgQUIgKHB1YmwpMREwDwYDVQQF\n" +
            "EwgxMjM0NTY3ODEXMBUGA1UEKQwOVGVzdCBhdiBCYW5rSUQxFjAUBgNVBAMMDUZQ\n" +
            "IFRlc3RjZXJ0IDIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCsr/aP\n" +
            "4Xwwl3dHsdOSEPncEOH/8qdM6Ghmg5uq0hQ9rD+7hUHboNQeQUYO5w8/6P1yEwAg\n" +
            "6Q8YWdaHdGJX1BzHQuYKIRluF3lt+AP0bVj9N8NM8Zq5Nj4A1Cwynt70ZixT5RLe\n" +
            "Sd003APbcpr2RyuZlNZeExCgHz+ZdRFlca2C37GiJZwoCuV87x1w3q+pRMZvyqWO\n" +
            "61W5Kw3OOkYIMaWq0aMSleW95JvWoZOT8fr2bJZoewWM38DxtsX5fNKlUAY5bWbt\n" +
            "YNrRdy8xZm2MFeZXucDZbZajKmgTeYS0liSiyo4AS77KhlWHFZB0BPWY8PM7reg3\n" +
            "6Qz3bEc5A6vtBvbzAgMBAAGjZTBjMBEGA1UdIAQKMAgwBgYEKgMEBTAOBgNVHQ8B\n" +
            "Af8EBAMCB4AwHQYDVR0OBBYEFJZci5tuY/wQO4yXQLA4XzsGEDfcMB8GA1UdIwQY\n" +
            "MBaAFOK5VQi8YqBGOV2ADFBKkgHLTO+LMA0GCSqGSIb3DQEBCwUAA4ICAQBxY+Vm\n" +
            "v/wR/fJlGgnXv6rKPxnSMWo0vj9xL1sxYjXkTqqUCE0wU6KFX6h93LsDiqUhw8F2\n" +
            "9Iwld+yet3m3c2Oy4MJQhm4Gs0OtzlO+Wq4e8rk7OtZvkkd26lpnC9ZDu8n2qvmw\n" +
            "VzWr07ZnI/D8NiLNFjR+frrftQmFnAxHuXHqwZk0PCjf7Y8ZJtsCyLp65/ecSIGV\n" +
            "p6sa1fWKK0zODpoSZhmca/fi1yfcoA+RhuHTtwi6PKAXpBU1/29y/hO7/UDw8cT3\n" +
            "ekNFGdL+dMYKoUN/P1sdyE+NGRaP3rnzwAjuoH3Dd+Em86DrL/8+1Smbng4FSQFI\n" +
            "wBe7Pxv6s4UFFk5A8K5wm3KwHnfNgDG3yT7Xv1nE8Y7Aai0fpMONCsCqT/ni+4Eb\n" +
            "trt1Ol767Cd7FPKkT2xroLne3toDj/b3ENCuVTnuoDpcA9dDl+5rlUJk4yKNAH9Z\n" +
            "JdZcmVFiBeFLvBIvdgoXFD3ll69lB9BERYaNnZVW5gCHL/i43dxwuo7zawHy+p0/\n" +
            "AwSqDF2JlWun5/c6VfnbM1hOBFJNKSxZv6om+QvY9jJGKT1DkFmHSPv7LM/ZcoTM\n" +
            "XJHTDcnyw9XbLkn04iXWLX5gITemJGAt9XAoaXXcmV2awxIDZHGVX3115V9Dd8Ir\n" +
            "kjLZow6Ct9QGdaglMcsoxnj1iSXc0B3ohNGtRQ==\n" +
            "-----END CERTIFICATE-----\n";


    private final URI authEndpoint;
    private final URI signEndpoint;
    private final URI collectEndpoint;
    private final URI cancelEndpoint;
    private final BankIDNode.EnvironmentType environmentType;

    private BankIDEnvironment(String wellKnownEndpoint, BankIDNode.EnvironmentType environmentType) {
        this.authEndpoint = URI.create(wellKnownEndpoint + "auth");
        this.signEndpoint = URI.create(wellKnownEndpoint + "sign");
        this.collectEndpoint = URI.create(wellKnownEndpoint + "collect");
        this.cancelEndpoint = URI.create(wellKnownEndpoint + "cancel");
        this.environmentType = environmentType;
    }

    public static BankIDEnvironment newInstance(BankIDNode.EnvironmentType environmentType) {
        if (environmentType == BankIDNode.EnvironmentType.PROD) {
            return new BankIDEnvironment(
                    "https://appapi2.bankid.com/rp/v5/",
                    environmentType
            );
        }
        return new BankIDEnvironment(
                "https://appapi2.test.bankid.com/rp/v5/",
                environmentType
        );
    }

    public URI getAuthEndpoint() {
        return authEndpoint;
    }

    public URI getSignEndpoint() {
        return signEndpoint;
    }

    public URI getCollectEndpoint() {
        return collectEndpoint;
    }

    public URI getCancelEndpoint() {
        return cancelEndpoint;
    }

    public String getCertificateChain(String chain) {
        switch (environmentType) {
            case TEST:
                if (StringUtils.isNotBlank(chain)) {
                    return chain + "\n" + testCA;
                } else {
                    return testKeyPair + "\n" + testCA;
                }
            case PROD:
                return chain + "\n" + prodCA;
        }
        return "";
    }
}



