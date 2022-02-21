---

-- 获取方法签名特征
local methodKey = KEYS[1]
-- 打印日志，这个会打印在 redis 服务端的日志中
redis.log(redis.LOG_DEBUG, "key is", methodKey)

-- 调用脚本传入的限流大小
local limit = tonumber((ARGV[1]))

-- 过去一秒中调用次数是多少
-- 从 redis 中获取值，如果获取不到则返回 0，然后将字符串转成数字类型
local count = tonumber(redis.call('get', methodKey) or "0")

-- 是否超出限流阈值
if count + 1 > limit then
    -- 被限流
    return false
else
    -- 没有超过阈值
    -- 使用 redis 的函数来执行
    redis.call("INCRBY", methodKey, 1)
    -- 设置过期时间，1 秒后过期
    redis.call("EXPIRE", methodKey, 1)
    return true;
end