import{_ as p}from"./plugin-vue_export-helper-c27b6911.js";import{r as l,o,c as i,b as n,d as a,e,a as t}from"./app-54cd880a.js";const c={},u=n("h1",{id:"_2-项目部署",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#_2-项目部署","aria-hidden":"true"},"#"),a(" 2 项目部署")],-1),r=n("p",null,"项目前后端分离，可以使用Docker Compose进行部署，或者clone项目到本地后分别打包后部署",-1),d={href:"https://barragefly.ordinaryroad.tech:7000",target:"_blank",rel:"noopener noreferrer"},k=n("li",null,"WebSocket地址：wss://barragefly.ordinaryroad.tech:7100",-1),v={href:"https://pd.qq.com/s/3id0n7fvs",target:"_blank",rel:"noopener noreferrer"},m=n("h2",{id:"_2-1-docker-compose",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#_2-1-docker-compose","aria-hidden":"true"},"#"),a(" 2.1 Docker Compose")],-1),y=n("blockquote",null,[n("p",null,[a("Docker Compose示例位于"),n("code",null,".docker"),a("目录中")])],-1),b=n("li",null,[a("arm64系统时，后端镜像请使用 "),n("code",null,"ordinaryroad-barrage-fly-arm64")],-1),_=n("li",null,[a("使用mysql、nginx时需要先build "),n("ul",null,[n("li",null,[n("code",null,"docker compose build ordinaryroad-barrage-fly-mysql")]),n("li",null,[n("code",null,"docker compose build ordinaryroad-barrage-fly-nginx")])])],-1),g={href:"https://www.bejson.com/enc/rsa/",target:"_blank",rel:"noopener noreferrer"},S=n("code",null,"-----BEGIN XXXXX KEY-----",-1),h=n("code",null,"-----END XXXXX KEY-----",-1),f={href:"https://ordinaryroad.coding.net/public-artifacts/ordinaryroad-barrage-fly/docker-pub/packages",target:"_blank",rel:"noopener noreferrer"},A=t(`<div class="language-bash line-numbers-mode" data-ext="sh"><pre class="language-bash"><code><span class="token comment"># DOCKER_OPTS=&quot;--registry-mirror=https://mirror.ccs.tencentyun.com&quot;</span>
<span class="token comment"># 拉取最新版1.2.0</span>
<span class="token function">docker</span> pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:1.2.0
<span class="token function">docker</span> pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64:1.2.0
<span class="token function">docker</span> pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:1.2.0
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,1),R=t(`<blockquote><p>拉到本地后需要根据compose文件重命名镜像</p><div class="language-bash line-numbers-mode" data-ext="sh"><pre class="language-bash"><code><span class="token function">docker</span> tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:1.2.0 ordinaryroad-barrage-fly
<span class="token function">docker</span> tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64:1.2.0 ordinaryroad-barrage-fly-arm64
<span class="token function">docker</span> tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:1.2.0 ordinaryroad-barrage-fly-ui
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div></blockquote><h3 id="_2-1-1-前端-后端" tabindex="-1"><a class="header-anchor" href="#_2-1-1-前端-后端" aria-hidden="true">#</a> 2.1.1 前端+后端</h3><div class="language-yaml line-numbers-mode" data-ext="yml"><pre class="language-yaml"><code><span class="token comment"># 需要的环境变量，其他可以环境变量可看每个目录下的.env文件，初始化sql位于\`.docker/ordinaryroad-barrage-fly-mysql/sql/or_barrage_fly.sql\`</span>
<span class="token comment"># MYSQL_HOST（注意请勿用localhost、127.0.0.1）</span>
<span class="token comment"># MYSQL_PORT</span>
<span class="token comment"># MYSQL_DATABASE</span>
<span class="token comment"># MYSQL_USERNAME</span>
<span class="token comment"># MYSQL_PASSWORD</span>
<span class="token comment"># ADMIN_USERNAME（任务管理后台登录用户名，默认admin）</span>
<span class="token comment"># ADMIN_PASSWORD（任务管理后台登录密码，默认admin）</span>
<span class="token comment"># SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin的地址）</span>
<span class="token comment"># SUB_BASE_URL（根据后端部署情况设置Client要连接的WebSocket地址）</span>

<span class="token key atrule">version</span><span class="token punctuation">:</span> <span class="token string">&quot;3.0&quot;</span>
<span class="token key atrule">services</span><span class="token punctuation">:</span>
  <span class="token key atrule">ordinaryroad-barrage-fly-ui</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">:</span><span class="token number">8080</span>
      <span class="token key atrule">SPRING_BOOT_ADMIN_BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//localhost<span class="token punctuation">:</span>8080/admin
      <span class="token key atrule">SUB_BASE_URL</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;30000:3000&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_HOST</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_PORT</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_DATABASE</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">JAVA_OPTS</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;8080:8080&quot;</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;9898:9898&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_2-1-2-前端-后端-mysql" tabindex="-1"><a class="header-anchor" href="#_2-1-2-前端-后端-mysql" aria-hidden="true">#</a> 2.1.2 前端+后端+MySQL</h3><div class="language-yaml line-numbers-mode" data-ext="yml"><pre class="language-yaml"><code><span class="token comment"># 需要的环境变量，其他可以环境变量可看每个目录下的.env文件</span>
<span class="token comment"># MYSQL_ROOT_PASSWORD（MySQL初始ROOT密码）</span>
<span class="token comment"># ADMIN_USERNAME（任务管理后台登录用户名，默认admin）</span>
<span class="token comment"># ADMIN_PASSWORD（任务管理后台登录密码，默认admin）</span>
<span class="token comment"># SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin的地址）</span>
<span class="token comment"># SUB_BASE_URL（根据后端部署情况设置Client要连接的WebSocket地址）</span>

<span class="token key atrule">version</span><span class="token punctuation">:</span> <span class="token string">&quot;3.0&quot;</span>
<span class="token key atrule">services</span><span class="token punctuation">:</span>
  <span class="token key atrule">ordinaryroad-barrage-fly-mysql</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">volumes</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> $PWD/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql/etc/my.cnf<span class="token punctuation">:</span>/etc/my.cnf
      <span class="token punctuation">-</span> $PWD/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql/data<span class="token punctuation">:</span>/var/lib/mysql
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_ROOT_PASSWORD</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;33066:3306&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly-ui</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">:</span><span class="token number">8080</span>
      <span class="token key atrule">SPRING_BOOT_ADMIN_BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//localhost<span class="token punctuation">:</span>8080/admin
      <span class="token key atrule">SUB_BASE_URL</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;30000:3000&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_USERNAME</span><span class="token punctuation">:</span> root
      <span class="token key atrule">MYSQL_PASSWORD</span><span class="token punctuation">:</span> $<span class="token punctuation">{</span>MYSQL_ROOT_PASSWORD<span class="token punctuation">}</span>
      <span class="token key atrule">ADMIN_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">JAVA_OPTS</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;8080:8080&quot;</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;9898:9898&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_2-1-3-前端-后端-mysql-nginx" tabindex="-1"><a class="header-anchor" href="#_2-1-3-前端-后端-mysql-nginx" aria-hidden="true">#</a> 2.1.3 前端+后端+MySQL+Nginx</h3><div class="language-yaml line-numbers-mode" data-ext="yml"><pre class="language-yaml"><code><span class="token comment"># 需要的环境变量，其他可以环境变量可看每个目录下的.env文件</span>
<span class="token comment"># MYSQL_ROOT_PASSWORD（MySQL初始ROOT密码）</span>
<span class="token comment"># ADMIN_USERNAME（任务管理后台登录用户名，默认admin）</span>
<span class="token comment"># ADMIN_PASSWORD（任务管理后台登录密码，默认admin）</span>
<span class="token comment"># SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin的地址）</span>
<span class="token comment"># SUB_BASE_URL（根据后端部署情况设置Client要连接的WebSocket地址）</span>

<span class="token key atrule">version</span><span class="token punctuation">:</span> <span class="token string">&quot;3.0&quot;</span>
<span class="token key atrule">services</span><span class="token punctuation">:</span>
  <span class="token key atrule">ordinaryroad-barrage-fly-mysql</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">volumes</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> $PWD/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql/etc/my.cnf<span class="token punctuation">:</span>/etc/my.cnf
      <span class="token punctuation">-</span> $PWD/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql/data<span class="token punctuation">:</span>/var/lib/mysql
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_ROOT_PASSWORD</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;33066:3306&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly-ui</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">:</span><span class="token number">8080</span>
      <span class="token key atrule">SPRING_BOOT_ADMIN_BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//localhost<span class="token punctuation">:</span>8080/admin
      <span class="token key atrule">SUB_BASE_URL</span><span class="token punctuation">:</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_USERNAME</span><span class="token punctuation">:</span> root
      <span class="token key atrule">MYSQL_PASSWORD</span><span class="token punctuation">:</span> $<span class="token punctuation">{</span>MYSQL_ROOT_PASSWORD<span class="token punctuation">}</span>
      <span class="token key atrule">ADMIN_BASE_URL</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_PASSWORD</span><span class="token punctuation">:</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly-nginx</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>nginx
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>nginx
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>nginx
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token comment"># UI</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;81:81&quot;</span>
      <span class="token comment"># Admin</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;8080:8080&quot;</span>
      <span class="token comment"># 后端</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;8081:8081&quot;</span>
      <span class="token comment"># RSocket</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;9898:9898&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>nginx
    <span class="token key atrule">depends_on</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
      <span class="token punctuation">-</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_2-1-4-example" tabindex="-1"><a class="header-anchor" href="#_2-1-4-example" aria-hidden="true">#</a> 2.1.4 Example</h3><p>新建一个文件夹，文件夹内只需要一个<code>.env</code>文件和一个<code>compose.yaml</code>文件</p><h4 id="env" tabindex="-1"><a class="header-anchor" href="#env" aria-hidden="true">#</a> .env</h4><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token comment"># .env</span>
<span class="token comment"># TODO 根据实际部署情况修改</span>
<span class="token key attr-name">MYSQL_HOST</span><span class="token punctuation">=</span><span class="token value attr-value">192.168.1.2</span>
<span class="token key attr-name">MYSQL_PORT</span><span class="token punctuation">=</span><span class="token value attr-value">3306</span>
<span class="token key attr-name">MYSQL_DATABASE</span><span class="token punctuation">=</span><span class="token value attr-value">or_barrage_fly</span>
<span class="token key attr-name">MYSQL_USERNAME</span><span class="token punctuation">=</span><span class="token value attr-value">root</span>
<span class="token key attr-name">MYSQL_PASSWORD</span><span class="token punctuation">=</span><span class="token value attr-value">root</span>
<span class="token comment"># TODO 根据实际部署情况修改</span>
<span class="token key attr-name">SUB_BASE_URL</span><span class="token punctuation">=</span><span class="token value attr-value">ws://localhost:9898</span>
<span class="token comment"># TODO 管理后台的账号密码</span>
<span class="token key attr-name">ADMIN_USERNAME</span><span class="token punctuation">=</span><span class="token value attr-value">admin</span>
<span class="token key attr-name">ADMIN_PASSWORD</span><span class="token punctuation">=</span><span class="token value attr-value">admin</span>
<span class="token comment"># TODO 根据实际需要修改</span>
<span class="token key attr-name">JAVA_OPTS</span><span class="token punctuation">=</span><span class="token value attr-value">&quot;-Xmn256m&quot;</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h4 id="compose-yaml" tabindex="-1"><a class="header-anchor" href="#compose-yaml" aria-hidden="true">#</a> compose.yaml</h4><div class="language-yaml line-numbers-mode" data-ext="yml"><pre class="language-yaml"><code><span class="token comment"># compose.yaml</span>
<span class="token key atrule">version</span><span class="token punctuation">:</span> <span class="token string">&quot;3.0&quot;</span>
<span class="token key atrule">services</span><span class="token punctuation">:</span>
  <span class="token key atrule">ordinaryroad-barrage-fly-ui</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">:</span><span class="token number">8080</span>
      <span class="token key atrule">SPRING_BOOT_ADMIN_BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//localhost<span class="token punctuation">:</span>8080/admin
      <span class="token key atrule">SUB_BASE_URL</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;30000:3000&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_HOST</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_PORT</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_DATABASE</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">JAVA_OPTS</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token comment"># 用于访问Spring Boot Admin</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;8080:8080&quot;</span>
      <span class="token comment"># 用于连接RSocket</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;9898:9898&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="_2-2-本地打包运行" tabindex="-1"><a class="header-anchor" href="#_2-2-本地打包运行" aria-hidden="true">#</a> 2.2 本地打包运行</h2><ol><li>clone项目到本地</li><li>后端打包 <code>./gradlew clean bootJar</code></li><li>前端打包 <code>npm run build</code></li></ol>`,15);function q(M,O){const s=l("ExternalLinkIcon");return o(),i("div",null,[u,r,n("ul",null,[n("li",null,[a("演示DEMO："),n("a",d,[a("https://barragefly.ordinaryroad.tech:7000"),e(s)])]),k]),n("blockquote",null,[n("p",null,[a("帐号请前往"),n("a",v,[a("QQ频道"),e(s)]),a("获取")])]),m,y,n("ul",null,[b,_,n("li",null,[a("在线生成RSA密钥对："),n("a",g,[a("https://www.bejson.com/enc/rsa/"),e(s)]),a(" （不需要第一行的"),S,a("和最后一行的"),h,a("）")])]),n("blockquote",null,[n("p",null,[a("镜像加速与拉取："),n("a",f,[a("仓库链接"),e(s)])]),A]),R])}const L=p(c,[["render",q],["__file","index.html.vue"]]);export{L as default};
