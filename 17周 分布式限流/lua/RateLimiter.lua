--
-- Created by IntelliJ IDEA.
-- User: tristeza
-- Date: 2020/8/9
-- Time: 6:59 下午
-- To change this template use File | Settings | File Templates.
--

-- 用作限流的Key
local key = 'My Key'
-- 限流的最大阈值
local limit = 2
--当前的流量大小
local currentLimit = 0

if currentLimit + 1 > limit then
    print 'reject'
    return false
else
    print 'accept'
    return true
end


