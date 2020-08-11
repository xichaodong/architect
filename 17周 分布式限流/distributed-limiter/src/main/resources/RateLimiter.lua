--
-- Created by IntelliJ IDEA.
-- User: tristeza
-- Date: 2020/8/9
-- Time: 7:59 下午
-- To change this template use File | Settings | File Templates.
--
--获取方法签名
local methodKey = KEYS[1]
redis.log(redis.LOG_DEBUG, 'key is', methodKey)
--调用脚本传入的限流大小
local limit = tonumber(ARGV[1])
--获取当前流量大小
local count = tonumber(redis.call('get', methodKey) or '0')
--是否超出限流阈值
if count + 1 > limit then
    return false
else
    redis.call("INCRBY", methodKey, 1)
    redis.call("EXPIRE", methodKey, 1)
    return true
end
