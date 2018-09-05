package com.iotc.eft.client;

import com.iotc.eft.config.MixinClientConfiguration;
import com.iotc.eft.dto.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description mixin api接口调用列表
 */
@FeignClient(name = "mixin", url = "${mixin.api.url}" ,configuration = MixinClientConfiguration.class)
public interface MixinApiClient {

    /**
     * @description 获取用户充值地址及账户余额
     * @param assetsId 充值地址资产id，即对应币种的资产id
     * @return 返回充值地址
     */
    @RequestMapping(method = RequestMethod.GET, value = "/assets/{assetsId}")
    Response<AssetAddress> assets(@PathVariable("assetsId") String assetsId);

    /**
     * @descrption 更新转账密码，初始值为空
     * @param payload 更新信息，json串
     * @return 返回用户信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/pin/update")
    Response<MixinUser> pinUpdate(@RequestBody String payload);

    /**
     * @description 转账
     * @param payload 转账信息，json串
     * @return 返回转账后的状态等信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/transfers")
    Response<Transfer> transfer(@RequestBody String payload);

    /**
     * @description 添加体现地址
     * @param payload 请求信息体，json串
     * @return 返回体现地址映射的内容
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addresses")
    Response<WithdrawalAddress> addAddress(@RequestBody String payload);

    /**
     * @description 提现
     * @param payload 提现请求内容，json串
     * @return 返回提现内容
     */
    @RequestMapping(method = RequestMethod.POST, value = "/withdrawals")
    Response<Withdrawal> withdrawals(@RequestBody String payload);


    /**
     * @description 查询用户信息
     * @param identity_number mixin用户id或是手机号
     * @return 返回用户信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search/{identity_number}")
    Response<MixinUser> searchUser(@PathVariable("identity_number") String identity_number);

    /**
     * @description 拉取mixin交易数据
     * @param limit 条数
     * @param offset 时间，时间格式需要采用"yyyy-MM-dd'T'HH:mm:ss.SSS'000000'ZZZZZ"，例如2018-06-15T10:04:05.999999999Z07:00
     * @param asset 资产类型id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/network/snapshots")
    Response<List<Snapshot>> snapshots(@RequestParam("limit") String limit,
                                       @RequestParam("offset") String offset, @RequestParam("asset") String asset);

    /**
     * @description 拉取mixin交易数据
     * @param limit 条数
     * @param offset 时间，时间格式需要采用"yyyy-MM-dd'T'HH:mm:ss.SSS'000000'ZZZZZ"，例如2018-06-15T10:04:05.999999999Z07:00
     * @param asset 资产类型id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/network/snapshots")
    String snapshots1(@RequestParam("limit") String limit,
                                       @RequestParam("offset") String offset, @RequestParam("asset") String asset);

    /**
     * @description 查询用户信息
     * @param pin mixin用户id或是手机号
     * @return 返回用户信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/pin/verify")
    Response<MixinUser> pinVerify(@RequestBody String pin);


}
