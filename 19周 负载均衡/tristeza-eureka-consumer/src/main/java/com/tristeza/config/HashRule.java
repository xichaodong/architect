package com.tristeza.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author chaodong.xi
 * @date 2021/2/23 4:20 下午
 */
public class HashRule extends AbstractLoadBalancerRule implements IRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uri = String.format("%s?%s", request.getServletPath(), request.getQueryString());

        return route(uri.hashCode(), getLoadBalancer().getAllServers());
    }

    private Server route(int hashId, List<Server> addressList) {
        if (CollectionUtils.isEmpty(addressList)) {
            return null;
        }

        TreeMap<Long, Server> addressMapping = new TreeMap<>();

        addressList.forEach(address -> {
            for (int i = 0; i < 8; i++) {
                addressMapping.put(hash(address.getId() + i), address);
            }
        });

        long hash = hash(String.valueOf(hashId));
        SortedMap<Long, Server> last = addressMapping.tailMap(hash);

        if (CollectionUtils.isEmpty(last)) {
            return addressMapping.firstEntry().getValue();
        }

        return last.get(last.firstKey());
    }

    private long hash(String key) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] keyByte = key.getBytes(StandardCharsets.UTF_8);

        messageDigest.update(keyByte);
        byte[] digest = messageDigest.digest();

        return ((digest[0] & 0xFF) | (digest[1] & 0xFF << 8) | (digest[2] & 0xFF << 16));
    }
}
