package com.linfeng.educenter.service.impl;

import com.linfeng.educenter.entity.vo.RegisterVo;
import com.linfeng.educenter.service.UcenterMemberService;
import com.linfeng.educenter.utils.MD5;
import com.linfeng.commonutils.JwtUtils;
import com.linfeng.educenter.entity.UcenterMember;
import com.linfeng.educenter.mapper.UcenterMemberMapper;
import com.linfeng.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-09-26
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //登录的方法
    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();
        //手机号和密码非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new MyException(20001,"手机号或密码不能为空");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        //判断查询对象是否为空
        if(mobileMember == null) {//没有这个手机号
            throw new MyException(20001,"请输入正确的手机号");
        }
        //把输入的密码进行加密，再和数据库密码进行比较
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new MyException(20001,"密码错误");
        }
        //判断用户是否禁用
        if(mobileMember.getIsDisabled()) {
            throw new MyException(20001,"登录失败");
        }
        //返回token字符串
        return JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
    }

    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode(); //验证码
        String mobile = registerVo.getMobile(); //手机号
        String nickname = registerVo.getNickname(); //昵称
        String password = registerVo.getPassword(); //密码
        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)) {
            throw new MyException(20001,"注册失败");
        }
        //判断手机号是否重复，表里面存在相同手机号不进行添加
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0) {
            throw new MyException(20001,"手机号已存在");
        }
        //获取redis验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)) {
            throw new MyException(20001,"验证码错误");
        }
        //数据添加数据库中
        UcenterMember userInfo = new UcenterMember();
        userInfo.setMobile(mobile);
        userInfo.setNickname(nickname);
        userInfo.setPassword(MD5.encrypt(password));//密码需要加密的
        userInfo.setIsDisabled(false);//用户不禁用
        userInfo.setAvatar("https://education-linfeng.oss-cn-hangzhou.aliyuncs.com/2023/02/27/adminAvatar.jpg");
        baseMapper.insert(userInfo);
    }

    //判断数据表里面是否存在相同微信信息，根据openid判断
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> QueryWrapper = new QueryWrapper<>();
        //设置查询条件
        QueryWrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(QueryWrapper);

        return member;
    }

    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }


}
