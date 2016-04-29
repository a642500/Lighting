## scheme
`lightupjs://`

## body
json format string dictionary

## protocal
### general

```
{
    "type:" "hanlder type"
    "call": "<self_function name>"
    "args": {<arguments>}
}
```

### function
#### arguments

```
{
     "<arg1 name>": <any type>,
     "<arg2 name>": <any type>
}
```

#### return

evaluate javascript string
```
<self_function name>Return({<returned arguments>})
```

## example
### function call without return
jump
type: jump
call: self_function

```
lightupjs://{type:"jump","call":"self_jump","args":{"page":"message"}}
```

log

```
lightupjs://{type:"log","call":"self_log","args":{"text":"%E4%B8%AD%E6%96%87"}}
```

### function call with return
getAccount

```
lightupjs://{type:"getAccount","call":"self_getAccount","args":{}}
```

native side will execute javascript string

```
self_getAccount({"_id":"xxxx", "nickname": "xxxx", "gender": "xxx", "avatar": "http://xxx.com/xxx.png"})
```

```
lightupjs://{type:"getOthers","call":"self_getOthers","args":{}}
```
native side will execute javascript string

```
self_getOthers({"user_id":"xxxx", "access_token": "xxx", "target_id": "xxx"})
```
## apis
### log 日志接口
arguments
* text log信息
### getAccount 用户信息
no arguments
### jump
arguments
* page 需要jump到的页面
page值

```
update_profile 为更新个人信息界面
play_video 为视频播放页
play_audio 为音频播放页
other_profile 为其他用户的个人主页
audio_question_list 为音频问题页
video_question_list 为视频问题页
```

page的附加信息

```
play_video附加
user_id
type: experience/value 跳转到情感经历还是价值观

play_audio附加
user_id
type: basic 跳转到基本信息音频播放

other_profile附加
user_id 用户id
```

example

```
lightup://{"type":"jump","args":{"page":"play_video","user_id":"xxx","type":"value"}}
```

### getOthers
no arguments
* 获取其他用户的id，以获取其他用户信息
### getAccessToken
* 获取当前用户的accessToken
args
user_id：如果没有这个的话 就默认获取当前用户的access token

```
{
    "user_id": "<user_id>"
}
```

```
{
    "user_id" = "xxx",
    "access_token" = "xxx"
}
```

### config
配置webview

```
{
    "bounces": true/false, // iOS 页面的下拉上拉弹性 默认为false无弹性
    "h_scroll_indicator": true/false, // 页面水平滚动条 默认为false 不显示
    "v_scroll_indicator": true/false, // 页面垂直滚动条 默认为false
}
```

###personal
* 进入webview调用
```
willappear() no argue
```
* 完成上传后，跳转视频音频显示页
```
page : play_video
user_id : ""
type : experience/value 跳转到情感经历还是价值观

page : play_audio
user_id : ""
type : basic 跳转到基本信息音频播放
```
* 未完成上传，跳转问题列表页 
```
page:video_question_list
user_id:""
type: experience/value 跳转到情感经历还是价值观

page : audio_question_list 
user_id : ""
type : basic 跳转到基本信息音频播放
```
