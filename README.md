# iotc-eft
iOTC与币币交易所用户资金一键快速划转通道

### 1. 简介
为实现用户资金在iOTC场外交易所和币币交易所之间快速来回划转，通过采用第三方Mixin钱包进行中转，即iOTC场外交易所和币币交易所各自在Mixin上开设一个公司账户，并且充值一部分备用金，当用户进行资金划转时，直接通过公司账户来进行结算，从而实现用户资金的快速划转，具体实现流程请参考以下说明。

### 2. 实现流程介绍
快速划转通道具体实现流程如下：
#### 2.1 用户在iOTC场外交易所一键划转到币币交易所
a、用户在iOTC场外交易所页面发起一键划转(在对应的币币交易所网站下)；<br/>
b、iOTC场外交易所根据用户请求，通过Mixin中转，从iOTC在Mixin上的公司账户转账给币币交易所在Mixin上开设的公司账户，在转账记录中包含对应用户的信息（为加密信息，存放在Mixin备注信息字段中），Mixin上转账成功后，iOTC将对用户在场外交易所的资金进行扣减；<br/>
c、币币交易所从Mixin上定时获取转账记录，收到转账记录后，解密出用户信息，根据用户信息对用户在币币交易所中的账户进行资金增加，即完成此次划转。<br/>
#### 2.2 用户在币币交易所一键划转到iOTC场外交易所
a、用户在币币交易所页面发起一键划转；<br/>
b、币币交易所根据用户请求，通过Mixin中转，从币币交易所在Mixin上的公司账户转账给iOTC场外交易所在Mixin上开设的公司账户，在转账记录中包含对应用户的信息（为加密信息），Mixin上转账成功后，币币交易所即可对用户在币币交易所的资金进行扣减；<br/>
c、iOTC交易所从Mixin上定时获取转账记录，收到转账记录后，解密出用户信息，根据用户信息对用户在iOTC场外交易所中的账户进行资金增加，即完成此次划转。<br/>

注：币币交易所进行对账时只需拉取Mixin上的转账记录和在Mixin上的资金总额进行计算核对即可。

### 3. 对接流程介绍
a、币币交易所先在Mixin钱包上开设一个钱包账户，即Mixin上的机器人账户，在开设过程中会有账户私钥等信息，币币自行保管，建议币币交易所在使用账户私钥时，以加密方式缓存到项目内存中，不在项目中存储，提供加密接口，在项目部署过程中由CEO动态输入；<br/>
b、iOTC以加密邮件给币币发送一私钥和一对称加密密钥（采用AES对称加密），私钥用于授权等接口签名，密钥用于对Mixin上传输的用户信息进行加密解密，备注用户信息采用AES加密后再进行base64编码存放在Mixn备注信息字段上；<br/>
c、将此EFT项目部署到内部服务器，并设置入口出口和入口网关（只出不进）及内网访问IP,提高安全性（此项目采用Java语言封装了和Mixin交易的所有流程，直接部署即可使用，此版本仅支持单点部署，如果开发语言和币币语言不一样，币币可自行考虑开发整套划转流程，Mixin开发参考文档：https://gist.github.com/myrual/64769acd3d09e9fd3ac37636d899f844  https://developers.mixin.one/api）；<br/>
d、币币交易所开发一键划转逻辑(从币币划转到iOTC)，在币币项目中调用如上部署的EFT服务转账接口，返回成功状态即为成功划转，在调用过程中iOTC给予的密钥对用户信息进行加密,存放于Mixin备注信息字段中，iOTC获取到后会以密钥进行解密，然后对用户场外账户资金进行增加；<br/>
e、币币交易所开发定时拉取Mixin转账记录功能，在定时项目中定时调用如上部署的EFT项目拉取记录接口，拉取到转账记录后,采用iOTC给予的密钥进行解密用户信息，并对用户在币币上的资金进行增加（注：Mixin的转账记录采用的是倒排拉取的方式，如指定一个时间点2018-08-28，拉取100条，这100条记录为2018-08-28以前的记录）。<br/>

### 4. 用户信息加密方式
为提高资金划转安全性，用户信息将以加密后的方式存放在Mixin转账的备注信息中，用户信息先采用AES加密，然后对加密信息进行base64处理，再存放到Mixin的备注字段上；同理，在对其进行解密时，先解码base64,然后用密钥对其进行AES解密；<br/>
##### 用户信息格式如下：
	字段名称：合作商ID,用户手机号,国家码,币种类型,金额
	备注内容：100000,18618356888,86,ETH,0.01
	*以上字段为必包含字段

##### 如上内容测试案例：
	测试密钥：aosnxyfjgpslx
	加密后的内容：k/c7ZHP7U3hdzOD16lKnJlcd7vTYJCTFN2XvoXdYee4=

##### AES加密JAVA示例代码：
    /**
     * @description 采用AES加密
     * @param password 加密的密钥
     * @param content 需要加密的内容
     * @return 返回加密后的base64串
     */
    public static String encode(String password,String content){
        try {
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            keygen.init(128, random);
            SecretKey original_key=keygen.generateKey();
            byte [] raw=original_key.getEncoded();
            SecretKey key=new SecretKeySpec(raw, "AES");
            Cipher cipher=Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] byte_encode=content.getBytes("utf-8");
            byte [] byte_AES=cipher.doFinal(byte_encode);
            String AES_encode=new String(new BASE64Encoder().encode(byte_AES));
            return AES_encode;
        } catch (Exception e) {
            log.error("AES encode error:{}",e);
        }
        return null;
    }

##### AES解密JAVA示例代码：
	/**
     * @description 采用AES解密
     * @param password 解密的密码
     * @param content 已经加密的内容
     * @return 返回解密后的内容
     */
    public static String decode(String password,String content){
        try {
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            keygen.init(128, random);
            SecretKey original_key=keygen.generateKey();
            byte [] raw=original_key.getEncoded();
            SecretKey key=new SecretKeySpec(raw, "AES");
            Cipher cipher=Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
            byte [] byte_decode=cipher.doFinal(byte_content);
            String AES_decode=new String(byte_decode,"utf-8");
            return AES_decode;
        } catch (Exception e) {
            log.error("AES decode error:{}",e);
        }
        return null;
    }

### 5. EFT接口介绍
EFT项目为币币内网部署项目，接口调用不设计签名等方式

#### 5.1 接口模式
<a>本文档定义的接口模式为：直连接口，均为POST请求。</a>
##### 5.1.1 直连接口
<a>请求：币币交易所相关项目直接组装HTTPS报文发送至部署后的EFT服务,EFT完成处理后同步返回结果。<br/>
   返回：所有直连接口都同步返回。</a><br/>

##### 5.1.2 响应结果
	响应返回参数格式如下：
参数名称  | 必填 | 类型 | 长度 | 参数说明
------------- | ------------- | ------------- | ------------- | :-------------
code | Y | S | | 响应码，见【调用返回码】
message | N | S | 50 | 响应信息描述
data | Y | S | | 响应数据

附加：响应报文示例

	{
    	"code":"0",
   		"message":"成功",
    	"data":
    	   {
    	     "snapshotId":"9D55CFAF9BFEA98B090C34F26DBFD529",
    	     "assetType":"BTC"
    	   }
  	}


#### 5.2 接口介绍
##### 5.2.1 转账接口
	接口地址  | /eft/transfer
	接口请求参数：
参数名称  | 必填 | 是否唯一 | 长度 | 参数说明
------------- | ------------- | ------------- | ------------- | :-------------
assetType | Y |  是 |  |  币种类型，如BTC/ETC/USDT
toAccountId | Y |  是 |  |  iOTC在Mixin上的账号ID,为UUID
amount | Y |  否 | | 划转金额
orderId | Y |  是 | | 跟踪ID,即内部系统的UUID,便于跟踪记录
content | Y |  否 | | 用户加密后的信息

返回：

参数名称  | 必填 |  参数说明
------------- | ------------- | :-------------
snapshotId | Y |  Mixin转账唯一ID，可通过此ID在Mixin区块浏览器上查询交易
toAccountId | Y | Mixin上收款账号ID,即iOTC账号ID
assetType | Y | 币种类型BTC/ETH/USDT
amount | Y | 转账的金额
traceId | Y | 在请求时传入的跟踪ID

##### 5.2.2 请求响应报文示例

1、请求报文示例

	{
    	"assetType":"ETH",
    	"toAccountId":"e397b4e4-ad49-4af6-a86d-c404b7cab5b7",
    	"amount":"0.01",
    	"orderId":"a77b4e4-as49-3nf6-ai6d-c0p4b7aeb5l0",
    	"content":"k/c7ZHP7U3hdzOD16lKnJlcd7vTYJCTFN2XvoXdYee4="
  	}
  	
  	
 2、响应报文示例

	{
    	"code":"0",
   		"message":"成功",
    	"data":
    	   {
    	     "snapshotId":"29d190e2-3b65-4507-ae0d-b604bf1508b3",
    	     "toAccountId": "e397b4e4-ad49-4af6-a86d-c404b7cab5b7",
			 "assetType": "ETH",
			 "amount": "0.01",
			 "traceId": "a77b4e4-as49-3nf6-ai6d-c0p4b7aeb5l0"
    	   }
  	}

##### 5.2.2 拉取转入记录接口
	接口地址  | /eft/pullTransfeInRecord
	接口请求参数：
参数名称  | 必填 | 是否唯一 | 长度 | 参数说明
------------- | ------------- | ------------- | ------------- | :-------------
assetType | Y |  否 |  |  币种类型，如BTC/ETC/USDT
endTimeStamp | N |  否 |  |  毫秒时间戳，如不传，则为Mixin服务器最新时间
limit | N |  否 | | 拉取条数，如果不传，则默认100


返回：

参数名称  | 必填 |  参数说明
------------- | ------------- | :-------------
lastTimestamp | Y |  此次拉取记录最后一条的时间戳
recordList | Y | 记录列表，记录内容如下表

recordList记录参数：

参数名称  | 必填 |  参数说明
------------- | ------------- | :-------------
assetType | Y |  币种类型BTC/ETH/USDT
toAccountId | Y | Mixin上收款账号ID,即币币的账号ID
amount | Y | 转账金额
snapshotId | Y | Mixin转账唯一ID，可通过此ID在Mixin区块浏览器上查询交易
traceId | Y | 对方传入的跟踪ID,即iOTC传入的跟踪id
createTimeStamp | Y |  订单创建时间戳
content | Y | 用户信息加密串
opponenId | Y | 转账账号的Mixin账号ID,即来自哪个账户


##### 请求响应报文示例

1、请求报文示例

	{
    	"assetType":"ETH",
    	"endTimeStamp":"1536034866615",
    	"limit":"200"
  	}
  	
  	
 2、响应报文示例

	{
    	"code":"0",
   		"message":"成功",
    	"data":
    	   {
    	     "lastTimestamp":"1536034866615",
    	     "recordList": [{"assetType": "ETH","toAccountId": "8cc8abd5-ef4e-4cf0-a168-27d9ffcd0f35","amount": "0.05",
    	     "snapshotId": "78b83411-58c2-4aaf-952b-639a31b1823e","traceId": "0bcfad67-2b3d-4ef9-88b3-8e12c44a5101",
    	     "createTimeStamp": "1536034876603","content": "k/c7ZHP7U3hdzOD16lKnJlcd7vTYJCTFN2XvoXdYee4=",
    	     "opponenId": "e397b4e4-ad49-4af6-a86d-c404b7cab5b7"
    	   },{"assetType": "ETH","toAccountId": "8cc8abd5-ef4e-4cf0-a168-27d9ffcd0f35","amount": "0.01",
    	     "snapshotId": "13b83411-58c2-4aaf-952b-639a31b18290","traceId": "0bcfad67-2b3d-4ef9-88b3-8e12c44a5101",
    	     "createTimeStamp": "1536034876603","content": "k/c7ZHP7U3hdzOD16lKnJlcd7vTYJCTFN2XvoXdsayt",
    	     "opponenId": "e397b4e4-ad49-4af6-a86d-c404b7cab5b7"
    	   },{"assetType": "ETH","toAccountId": "8cc8abd5-ef4e-4cf0-a168-27d9ffcd0f35","amount": "0.1",
    	     "snapshotId": "98b83411-58c2-4aaf-952b-639a31c1853d","traceId": "0bcfad67-2b3d-4ef9-88b3-8e12c44a5101",
    	     "createTimeStamp": "1536034876603","content": "k/c7ZHP7U3hdzOD16lKnJlcd7vTYJCTFN2XvoXdYe945",
    	     "opponenId": "e397b4e4-ad49-4af6-a86d-c404b7cab5b7"
    	   }]
  	}
