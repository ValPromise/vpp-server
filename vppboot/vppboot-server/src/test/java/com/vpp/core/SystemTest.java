package com.vpp.core;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.vpp.Application;
import com.vpp.core.system.resource.bean.Resource;
import com.vpp.core.system.resource.mapper.ResourceMapper;
import com.vpp.core.system.role.mapper.UserRoleMapper;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class SystemTest {
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    public void test() throws Exception {
        String etime = "2018-03-25";
        List<Long> list = new ArrayList<Long>();
        list.add(1l);
        List<Resource> res = resourceMapper.findListByRoleIds(list);

        System.out.println(res);

        try {

            List<Long> roleIds = userRoleMapper.findRoleIdsByUserId(1l);
            System.out.println(roleIds);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
