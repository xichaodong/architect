package com.tristeza.user.web.controller.front;

import com.tristeza.cloud.common.utils.MobileEmailUtils;
import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.user.model.bo.AddressBO;
import com.tristeza.user.service.front.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@Api(value = "地址相关操作", tags = {"地址相关接口"})
@RestController
@RequestMapping("address")
public class AddressController {
    @Resource
    private AddressService addressService;

    @ApiOperation(value = "根据用户id查询地址", notes = "根据用户id查询地址")
    @GetMapping("list")
    public JsonResult query(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        return JsonResult.ok(addressService.queryAll(userId));
    }

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址")
    @PostMapping("add")
    public JsonResult add(@RequestBody AddressBO address) {
        JsonResult checkAddress = checkAddress(address, false);

        if (!Objects.isNull(checkAddress)) {
            return checkAddress;
        }

        addressService.addAddress(address);

        return JsonResult.ok();
    }

    @ApiOperation(value = "修改用户地址", notes = "修改用户地址")
    @PostMapping("update")
    public JsonResult update(@RequestBody AddressBO address) {
        JsonResult checkAddress = checkAddress(address, true);

        if (!Objects.isNull(checkAddress)) {
            return checkAddress;
        }

        addressService.updateAddress(address);

        return JsonResult.ok();
    }

    @ApiOperation(value = "删除用户地址", notes = "删除用户地址")
    @PostMapping("delete")
    public JsonResult delete(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(addressId)) {
            return JsonResult.errorMsg("地址id不能为空");
        }

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        addressService.deleteAddress(userId, addressId);

        return JsonResult.ok();
    }

    @ApiOperation(value = "设置用户默认地址", notes = "设置用户默认地址")
    @PostMapping("setDefault")
    public JsonResult setDefaultAddress(@RequestParam String userId, @RequestParam String addressId) {
        if (StringUtils.isBlank(addressId)) {
            return JsonResult.errorMsg("地址id不能为空");
        }

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        addressService.setDefaultAddress(userId, addressId);

        return JsonResult.ok();
    }

    private JsonResult checkAddress(AddressBO addressBO, boolean isUpdate) {
        if (isUpdate && StringUtils.isBlank(addressBO.getAddressId())) {
            return JsonResult.errorMsg("地址id不能为空");
        }

        if (StringUtils.isBlank(addressBO.getReceiver())) {
            return JsonResult.errorMsg("收货人不能为空");
        }

        if (StringUtils.isBlank(addressBO.getMobile())) {
            return JsonResult.errorMsg("手机号不能为空");
        }

        if (addressBO.getMobile().length() != 11) {
            return JsonResult.errorMsg("手机号长度不正确");
        }

        if (!MobileEmailUtils.checkMobileIsOk(addressBO.getMobile())) {
            return JsonResult.errorMsg("手机号格式不正确");
        }

        if (addressBO.getReceiver().length() > 12) {
            return JsonResult.errorMsg("收货人姓名过长");
        }

        if (StringUtils.isBlank(addressBO.getProvince()) || StringUtils.isBlank(addressBO.getCity())
                || StringUtils.isBlank(addressBO.getDistrict()) || StringUtils.isBlank(addressBO.getDetail())) {
            return JsonResult.errorMsg("收货地址不能为空");
        }
        return null;
    }
}
