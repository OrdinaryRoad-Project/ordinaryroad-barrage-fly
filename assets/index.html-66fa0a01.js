import{_ as e}from"./plugin-vue_export-helper-c27b6911.js";import{r as t,o as p,c as l,b as n,d as a,e as o,a as c}from"./app-862a4d3c.js";const i={},u=n("h1",{id:"_2-项目部署",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#_2-项目部署","aria-hidden":"true"},"#"),a(" 2 项目部署")],-1),r=n("p",null,"项目前后端分离，可以使用Docker Compose进行部署，或者分别打包后部署",-1),d=n("h2",{id:"_2-1-docker-compose",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#_2-1-docker-compose","aria-hidden":"true"},"#"),a(" 2.1 Docker Compose")],-1),k=n("blockquote",null,[n("p",null,[a("Docker Compose示例位于"),n("code",null,".docker"),a("目录中")])],-1),v=n("li",null,[a("arm64系统时，后端镜像请使用 "),n("code",null,"ordinaryroad-barrage-fly-arm64")],-1),m=n("li",null,[a("使用mysql、nginx时需要先build "),n("ul",null,[n("li",null,[n("code",null,"docker compose build ordinaryroad-barrage-fly-mysql")]),n("li",null,[n("code",null,"docker compose build ordinaryroad-barrage-fly-nginx")])])],-1),y={href:"https://www.bejson.com/enc/rsa/",target:"_blank",rel:"noopener noreferrer"},b=c(`<blockquote><p>镜像加速与拉取</p><div class="language-bash line-numbers-mode" data-ext="sh"><pre class="language-bash"><code><span class="token comment"># DOCKER_OPTS=&quot;--registry-mirror=https://mirror.ccs.tencentyun.com&quot;</span>
<span class="token comment"># 默认拉取最新版</span>
<span class="token function">docker</span> pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly
<span class="token function">docker</span> pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64
<span class="token function">docker</span> pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div></blockquote><blockquote><p>拉到本地后需要根据compose文件重命名镜像</p><div class="language-bash line-numbers-mode" data-ext="sh"><pre class="language-bash"><code><span class="token function">docker</span> tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly ordinaryroad-barrage-fly
<span class="token function">docker</span> tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64 ordinaryroad-barrage-fly-arm64
<span class="token function">docker</span> tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui ordinaryroad-barrage-fly-ui
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div></blockquote><h3 id="_2-1-1-前端-后端" tabindex="-1"><a class="header-anchor" href="#_2-1-1-前端-后端" aria-hidden="true">#</a> 2.1.1 前端+后端</h3><div class="language-yaml line-numbers-mode" data-ext="yml"><pre class="language-yaml"><code><span class="token comment"># 需要的环境变量，其他可以环境变量可看每个目录下的.env文件，初始化sql位于\`.docker/ordinaryroad-barrage-fly-mysql/sql/or_barrage_fly.sql\`</span>
<span class="token comment"># MYSQL_HOST（注意请勿用localhost、127.0.0.1）</span>
<span class="token comment"># MYSQL_PORT</span>
<span class="token comment"># MYSQL_DATABASE</span>
<span class="token comment"># MYSQL_USERNAME</span>
<span class="token comment"># MYSQL_PASSWORD</span>
<span class="token comment"># SUB_BASE_URL（根据后端部署情况设置Client要连接的地址）</span>
<span class="token comment"># RSA_PUBLIC_KEY（RSA）</span>
<span class="token comment"># ADMIN_USERNAME（任务管理后台登录用户名）</span>
<span class="token comment"># ADMIN_PASSWORD（任务管理后台登录密码）</span>
<span class="token comment"># RSA_PRIVATE_KEY（RSA）</span>

<span class="token key atrule">version</span><span class="token punctuation">:</span> <span class="token string">&quot;3.0&quot;</span>
<span class="token key atrule">services</span><span class="token punctuation">:</span>
  <span class="token key atrule">ordinaryroad-barrage-fly-ui</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">volumes</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> $PWD/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui/app<span class="token punctuation">:</span>/ordinaryroad/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui/app
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">:</span><span class="token number">8080</span>
      <span class="token key atrule">RSA_PUBLIC_KEY</span><span class="token punctuation">:</span>
      <span class="token key atrule">SUB_BASE_URL</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;30000:3000&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_HOST</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_PORT</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_DATABASE</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">RSA_PUBLIC_KEY</span><span class="token punctuation">:</span>
      <span class="token key atrule">RSA_PRIVATE_KEY</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;9898:9898&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_2-1-2-前端-后端-mysql" tabindex="-1"><a class="header-anchor" href="#_2-1-2-前端-后端-mysql" aria-hidden="true">#</a> 2.1.2 前端+后端+MySQL</h3><div class="language-yaml line-numbers-mode" data-ext="yml"><pre class="language-yaml"><code><span class="token comment"># 需要的环境变量，其他可以环境变量可看每个目录下的.env文件</span>
<span class="token comment"># MYSQL_ROOT_PASSWORD（MySQL初始ROOT密码）</span>
<span class="token comment"># SUB_BASE_URL（根据后端部署情况设置Client要连接的地址）</span>
<span class="token comment"># RSA_PUBLIC_KEY（RSA）</span>
<span class="token comment"># ADMIN_USERNAME（任务管理后台登录用户名）</span>
<span class="token comment"># ADMIN_PASSWORD（任务管理后台登录密码）</span>
<span class="token comment"># RSA_PRIVATE_KEY（RSA）</span>

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
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">volumes</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> $PWD/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui/app<span class="token punctuation">:</span>/ordinaryroad/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui/app
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">:</span><span class="token number">8080</span>
      <span class="token key atrule">RSA_PUBLIC_KEY</span><span class="token punctuation">:</span>
      <span class="token key atrule">SUB_BASE_URL</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;30000:3000&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_USERNAME</span><span class="token punctuation">:</span> root
      <span class="token key atrule">MYSQL_PASSWORD</span><span class="token punctuation">:</span> $<span class="token punctuation">{</span>MYSQL_ROOT_PASSWORD<span class="token punctuation">}</span>
      <span class="token key atrule">ADMIN_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">RSA_PUBLIC_KEY</span><span class="token punctuation">:</span>
      <span class="token key atrule">RSA_PRIVATE_KEY</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;9898:9898&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_2-1-3-前端-后端-mysql-nginx" tabindex="-1"><a class="header-anchor" href="#_2-1-3-前端-后端-mysql-nginx" aria-hidden="true">#</a> 2.1.3 前端+后端+MySQL+Nginx</h3><div class="language-yaml line-numbers-mode" data-ext="yml"><pre class="language-yaml"><code><span class="token comment"># 需要的环境变量，其他可以环境变量可看每个目录下的.env文件</span>
<span class="token comment"># MYSQL_ROOT_PASSWORD（MySQL初始ROOT密码）</span>
<span class="token comment"># SUB_BASE_URL（根据后端部署情况设置Client要连接的地址）</span>
<span class="token comment"># RSA_PUBLIC_KEY（RSA）</span>
<span class="token comment"># ADMIN_USERNAME（任务管理后台登录用户名）</span>
<span class="token comment"># ADMIN_PASSWORD（任务管理后台登录密码）</span>
<span class="token comment"># RSA_PRIVATE_KEY（RSA）</span>

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
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>mysql
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly-ui</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">volumes</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> $PWD/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui/app<span class="token punctuation">:</span>/ordinaryroad/ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui/app
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">:</span><span class="token number">8080</span>
      <span class="token key atrule">RSA_PUBLIC_KEY</span><span class="token punctuation">:</span>
      <span class="token key atrule">SUB_BASE_URL</span><span class="token punctuation">:</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_USERNAME</span><span class="token punctuation">:</span> root
      <span class="token key atrule">MYSQL_PASSWORD</span><span class="token punctuation">:</span> $<span class="token punctuation">{</span>MYSQL_ROOT_PASSWORD<span class="token punctuation">}</span>
      <span class="token key atrule">ADMIN_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">RSA_PUBLIC_KEY</span><span class="token punctuation">:</span>
      <span class="token key atrule">RSA_PRIVATE_KEY</span><span class="token punctuation">:</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly-nginx</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>nginx
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>nginx
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>nginx
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;81:81&quot;</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;9898:9898&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>nginx
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h3 id="_2-1-4-example" tabindex="-1"><a class="header-anchor" href="#_2-1-4-example" aria-hidden="true">#</a> 2.1.4 Example</h3><div class="language-properties line-numbers-mode" data-ext="properties"><pre class="language-properties"><code><span class="token comment"># .env</span>
<span class="token key attr-name">MYSQL_HOST</span><span class="token punctuation">=</span><span class="token value attr-value">192.168.1.2</span>
<span class="token key attr-name">MYSQL_PORT</span><span class="token punctuation">=</span><span class="token value attr-value">3306</span>
<span class="token key attr-name">MYSQL_DATABASE</span><span class="token punctuation">=</span><span class="token value attr-value">or_barrage_fly</span>
<span class="token key attr-name">MYSQL_USERNAME</span><span class="token punctuation">=</span><span class="token value attr-value">root</span>
<span class="token key attr-name">MYSQL_PASSWORD</span><span class="token punctuation">=</span><span class="token value attr-value">root</span>
<span class="token key attr-name">SUB_BASE_URL</span><span class="token punctuation">=</span><span class="token value attr-value">ws://ordinaryroad-barrage-fly:9898</span>
<span class="token key attr-name">RSA_PUBLIC_KEY</span><span class="token punctuation">=</span><span class="token value attr-value">MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNqVTCHbPojzNaR5TwhFxeKcuP/Po4J8WAc5dz1pHQ8FasH/hrSWwoFGpTTo6tfTl0mnAotu4p93kSMe+K2pc2VqUJwCcFj9cD6rhaKfjdj7/Kd2rHH43mXPI+OtggzzOKOOPsaMP5/r2Dyooafa1ChYDuSmf2fDM53CSIx+KDUwIDAQAB</span>
<span class="token key attr-name">ADMIN_USERNAME</span><span class="token punctuation">=</span><span class="token value attr-value">admin</span>
<span class="token key attr-name">ADMIN_PASSWORD</span><span class="token punctuation">=</span><span class="token value attr-value">admin</span>
<span class="token key attr-name">RSA_PRIVATE_KEY</span><span class="token punctuation">=</span><span class="token value attr-value">MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAM2pVMIds+iPM1pHlPCEXF4py4/8+jgnxYBzl3PWkdDwVqwf+GtJbCgUalNOjq19OXSacCi27in3eRIx74ralzZWpQnAJwWP1wPquFop+N2Pv8p3ascfjeZc8j462CDPM4o44+xow/n+vYPKihp9rUKFgO5KZ/Z8MzncJIjH4oNTAgMBAAECgYEAs+ttoRzHJa8Rp+tzmy7Qd/hsg503ciUpHYUCfG68xmNcD90wQPvMyQuDMTpKi/A/cYkikhvnI4PCwW46N+mf2nJZEYG1DARTVj0lWaW+RqzerXG1Jg6v1WbgJwy5myZhFm9AOSb0OW3HskbRRyqxkiFX2Fr7ZKYXfrIWvooiVWECQQDnc0PPBpl5XPB+yGKepEatRwCMKdoIoD3R96Iykq9xowskafwob+gOn3mqEiH8YuDPOntcC4tqeLSzNiHYftKpAkEA43nN5SqvJZ3Gp/Vm4VY7DRAAT4cCxcxyYy3p1iuZf19y0UGdRrURQl4jx+I9RGSIy4v/hfpU05wnecvLh3KfmwJBANUz1pjUSXgEZv1C9aWRShHAP/7dZp1fjtLlvCG+AaM6P79RahiNzUP7H4XMokXth40dIBmQAOMZQct75/2YBdECQQCpwP0Y1pir/qkAME8dO+eHYPiKYJt+FosKXnoRXKoI9qbNaCTBXmBJ4czb3oaQImI/W/NM/ToOTIrdBmuVYcGfAkA6bkUO1fjjSC8N00xX1uGxJTcWwd9MQAffS+jm+C69lOyNW/azJjgXnmkbjKKB0kDcMDn6bkuYblcm4GOrVhqA</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><div class="language-yaml line-numbers-mode" data-ext="yml"><pre class="language-yaml"><code><span class="token comment"># compose.yaml</span>
<span class="token key atrule">version</span><span class="token punctuation">:</span> <span class="token string">&quot;3.0&quot;</span>
<span class="token key atrule">services</span><span class="token punctuation">:</span>
  <span class="token key atrule">ordinaryroad-barrage-fly-ui</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">BASE_URL</span><span class="token punctuation">:</span> http<span class="token punctuation">:</span>//ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">:</span><span class="token number">8080</span>
      <span class="token key atrule">RSA_PUBLIC_KEY</span><span class="token punctuation">:</span>
      <span class="token key atrule">SUB_BASE_URL</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;30000:3000&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>ui
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always

  <span class="token key atrule">ordinaryroad-barrage-fly</span><span class="token punctuation">:</span>
    <span class="token key atrule">image</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly<span class="token punctuation">-</span>arm64
    <span class="token key atrule">container_name</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">build</span><span class="token punctuation">:</span>
      <span class="token key atrule">context</span><span class="token punctuation">:</span> ./ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">environment</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_HOST</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_PORT</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_DATABASE</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">MYSQL_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_USERNAME</span><span class="token punctuation">:</span>
      <span class="token key atrule">ADMIN_PASSWORD</span><span class="token punctuation">:</span>
      <span class="token key atrule">RSA_PUBLIC_KEY</span><span class="token punctuation">:</span>
      <span class="token key atrule">RSA_PRIVATE_KEY</span><span class="token punctuation">:</span>
    <span class="token key atrule">ports</span><span class="token punctuation">:</span>
      <span class="token punctuation">-</span> <span class="token string">&quot;9898:9898&quot;</span>
    <span class="token key atrule">hostname</span><span class="token punctuation">:</span> ordinaryroad<span class="token punctuation">-</span>barrage<span class="token punctuation">-</span>fly
    <span class="token key atrule">restart</span><span class="token punctuation">:</span> always
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,11);function g(_,S){const s=t("ExternalLinkIcon");return p(),l("div",null,[u,r,d,k,n("ul",null,[v,m,n("li",null,[a("在线生成RSA密钥对："),n("a",y,[a("https://www.bejson.com/enc/rsa/"),o(s)]),a(" （不需要-----BEGIN PUBLIC KEY-----）")])]),b])}const R=e(i,[["render",g],["__file","index.html.vue"]]);export{R as default};
