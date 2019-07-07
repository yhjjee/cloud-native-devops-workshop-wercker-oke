# Cloud Native DevOps Hands-On Workshop - Wercker and OKE (Oracle Kubernetes Engine)

![](images/header.png)

## Introduction
본 핸즈온 워크샵은 마이크로 서비스 애플리케이션에 대한 빌드/테스트/배포 자동화를 위한 CI/CD 파이프라인을 구성하고, 이를 관리형 쿠버네티스 서비스에 배포하는 전반적인 과정을 다루고 있습니다. 본 과정을 통해서 오라클의 컨테이너 기반 CI/CD 서비스인 Wercker(워커)와 오라클의 컨테이너 서비스인 Oracle Kubernetes Engine (OKE) 및 Oracle Container Registry Service에 대한 경험을 해볼 수 있습니다. 

## Objectives
* Oracle Kubernetes Cluster 생성
* Wercker 환경 구성, 파이프라인 이해 및 빌드
* Kubernetes Container 배포

## Required Artifacts
* 인터넷 접속 가능한 랩탑 (Windows 10이상, Windows 10 이하 버전일 경우 Powershell 필요)
* GitHub 계정
* OCI (Oracle Cloud Infrastructure) 계정

## Steps
본 실습은 총 10단계로 구성되어 있습니다.

* **STEP 1**: Setup  
* **STEP 2**: OCI에서 Kubernetes Cluster 생성하기  
* **STEP 3**: kubectl과 oci-cli 설치하기  
* **STEP 4**: GitHub Repository 생성하기  
* **STEP 5**: Wercker 환경 구성하기  
* **STEP 6**: Wercker CI/CD Pipeline 구성하기  
* **STEP 7**: Wercker 파이프라인 실행하기  
* **STEP 8**: Oracle Container Registry (OCIR) 확인  
* **STEP 9**: Oracle Kubernetes Engine (OKE) 에 생성(배포)된 Pod와 Service 확인하기  
* **STEP 10**: 서비스 확인하기

***

## **STEP 1**: Setup

### GitHub 계정 생성
* https://github.com 에 접속해서 우측 상단 **Sign up**을 클릭합니다.
![](images/github_signup.png)

* 다음 내용을 입력하고 **Create an account** 클릭해서 계정 생성합니다. 검증 메일이 발송되기 때문에 정확한 이메일을 입력해야 합니다. GitHub으로 부터 수신받은 이메일에서 **Verify email address**를 클릭해서 계정 생성을 완료합니다.

    ![](images/github_create_account.png)

    ![](images/github_verify_email.png)

* 생성한 계정으로 **Sign in** 합니다.

    ![](images/github_signin.png)

    ![](images/github_login.png)
    
### Wercker 계정 생성
* https://app.wercker.com 에 접속합니다.
    > GitHub에 이미 로그인 되어 있다면 아래 로그인 화면이 아닌 **Authorize wercker** 화면을 볼 수 있습니다.

    ![](images/wercker_login_with_github.png)

* GitHub 아이디와 패스워드를 입력하고 **Sign in** 버튼을 클릭합니다.
    > GitHub에 이미 로그인 되어 있다면 아래 로그인 화면이 아닌 **Authorize wercker** 화면을 볼 수 있습니다.

    ![](images/wercker_login_with_github_2.png)
    
* **Authorize wercker** 버튼을 클릭합니다.
    > GitHub에 이미 로그인 되어 있다면 바로 **Authorize wercker** 화면을 볼 수 있습니다.

    ![](images/wercker_login_with_github_3.png)

* Wercker에서 사용할 이름과 이메일을 입력합니다. GitHub과 동일하게 입력해 줍니다.

    ![](images/wercker_login_with_github_4.png)

    ![](images/wercker_login.png)

### **STEP 2**: OCI에서 Kubernetes Cluster 생성하기
* 먼저 OCI에 로그인합니다. 아래 URL을 통해서 Seoul Region으로 접속합니다.
    > tenancy 명은 처음 Oracle Cloud Subscription 시에 명명한 이름을 사용합니다.

    * https://console.ap-seoul-1.oraclecloud.com/?tenant={tenancy명}

    > 두 가지 로그인 타입이 있습니다. OCI 전용 계정이 있으며, IDCS라는 계정 관리를 위한 클라우드 서비스와 연동 (Federation)해서 사용하는 계정이 있습니다. 생성한 계정 타입을 확인 후 관련 계정으로 로그인 합니다.

* 로그인 화면
    ![](images/oci-login.png)

* 좌측 상단의 햄버거 모양의 아이콘을 클릭합니다.
    ![](images/oci-console.png)

* 좌측 메뉴 중 **Developer Services** > **Container Clusters (OKE)** 선택합니다.

    ![](images/oci-menu-oke.png)

* OKE Cluster를 생성 할 Compartment를 선택합니다.
    > Compartment는 OCI에서 관리하는 리소스들을 그룹으로 묶어서 관리하기 위해 제공되는 기능입니다. 일반적으로 팀 단위로 리소스(Compute, Network, Storage등)를 관리하기 위한 목적으로 사용됩니다. Compartment 이름은 아래 스크린샷과 다를 수 있습니다.

    ![](images/oci-create-oke-cluster-compartment.png)

* OKE Cluster 생성을 위해 **Create Cluster**버튼을 클릭 합니다.

    ![](images/oci-create-oke-cluster.png)

* 다음과 같이 입력합니다.  Virtual Machine Gen2, 1 OCPU에 생성된 네트워크 서브넷당 한개의 쿠버네티스 노드를 생성합니다.
    > Compute Shape의 의미는 다음과 같습니다. VM.Standard2.1은 Virtual Machine Gen2, 1 OCPU를 의미합니다.

    * NAME: oke-cluster1
    * KUBERNETES VERSION: v1.12.7
    * QUICK CREATE: CHECK
    * SHAPE: VM.Standard2.1
    * QUANTITY PER SUBNET: 2

    <img src="images/oci-create-oke-cluster-creation.png" width="50%">

* 정상적으로 생성이 되면 생성된 노드의 상태가 ACTIVE가 됩니다. (대략 5~10분 소요)
  
  **생성 진행 과정**  
    ![](images/oci-oke-cluster-created.png)
    
  **ACTIVE 상태의 노드**  
    <img src="images/oci-created-oke-cluster.png" width="50%">

### **STEP 3**: kubectl과 oci-cli 설치하기
* kubectl 제공되는 Git Repository에서 Clone으로 다운로드 받은 c:\cloud-native-devops-workshop-wercker-oke 폴더안에도 같이 포함되어 있으므로 이 파일을 사용합니다. 
TO-DO 다운로드 할 수 있는 경로를 따로 지정해서 설치하도록 수정...

    > 선택사항) 참고로 kubectl 직접 설치를 하고 싶으면 아래 curl 명령어를 통해 설치 가능합니다. 다만, curl 명령어는 Windows Prompt (cmd)에서 실행 합니다. (PowerShell 에서는 옵션이 다름)
    > ```
    > curl -LO https://storage.googleapis.com/kubernetes-release/> release/v1.15.0/bin/windows/amd64/kubectl.exe
    > ```

* 클라이언트에서 OKE 접속을 위해서는 kubeconfig 파일을 생성해야 합니다. kubeconfig 파일을 얻는 과정은 OCI의 OKE Cluster 화면에서 Access **Kubeconfig** 버튼을 클릭하면 확인할 수 있습니다.

    **Access Kubeconfig 버튼** 
    ![](images/oci-oke-access-kubeconfig-1.png)

    **Kubeconfig를 얻기 위한 과정**
    ![](images/oci-oke-access-kubeconfig-2.png)

    > **위 내용은 oci-cli 설치 후 실행할 내용이므로 꼭 메모합니다.**

* Kubeconfig를 얻기 위해서는 먼저 **oci-cli**를 설치합니다. 먼저, Windows 좌측 하단의 검색 버튼을 클릭하고 **PowerShell**을 입력한 후 **Windows PowerShell**을 관리자 모드(중요)로 실행합니다.

    <img src="images/windows-search-powershell.png" width="50%">

    ![](images/windows-powershell.png)


* **Windows PowerShell**에서 다음과 같이 명령어를 실행하여 oci-cli를 설치합니다.

    > oci-cli 설치를 위해 Python이 자동으로 설치됩니다.
    ```
    # Set-ExecutionPolicy RemoteSigned

    # powershell -NoProfile -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/oracle/oci-cli/master/scripts/install/install.ps1'))"
    ```

* Python이 자동으로 설치가 완료되면 oci-cli 설치 경로를 지정해야 합니다. 경로는 기본 경로(c:\Users\사용자명\)에 설치해도 되지만, Windows의 경우 사용자명에 공백이 있으면 설치가 되지 않습니다. 사용자명에 공백이 있을 경우에는 다음과 같이 c:\oracle 경로에 설치합니다. (공백이 없으면 기본 경로에 설치)
    ```
    1. c:\oracle\oci-cli
    2. c:\oracle\bin
    3. c:\oracle\bin\oci-cli-scripts
    4. 추가 패키지 설치 여부 (설치 없이 엔터 치고 넘어갑니다)
    ```

* 설치가 완료되면 **PowerShell**을 종료 후 다시 시작해서(관리자 모드) oci-cli 설치를 확인합니다.
    ```
    # oci -v
    ```

* oci-cli 설치가 완료되면 Oracle Cloud Infrastructure와 연결을 위한 설정을 합니다. 이 때 필요한 정보를 얻는 방법과 설정 방법은 다음과 같습니다.
    1. User OCID  
    User OCID는 OCI Console 우측 상단의 사용자 아이콘을 클릭한 후 아이디를 선택하면 확인할 수 있습니다.
    ![](images/oci-get-user-ocid.png)
    oci-cli 설치를 위해 필요하기 때문에 User OCID를 복사해서 메모합니다.
    ![](images/oci-get-user-ocid-copy.png)
    
    2. Tenancy OCID
    Tenancy OCID는 OCI Console 우측 상단의 사용자 아이콘을 클릭한 후 Tenancy를 선택하면 확인할 수 있습니다.
    ![](images/oci-get-tenancy-ocid.png)
    oci-cli 설치를 위해 필요하기 때문에 Tenancy OCID를 복사해서 메모합니다.
    ![](images/oci-get-tenancy-ocid-copy.png)

    3. Region
    여기서는 서울 리전으로 지정합니다. (ap-seoul-1)

* oci-cli 설정을 진행합니다. **Windows Powershell(관리자 모드)** 에서 다음과 같이 입력합니다.
    ```
    # oci setup config
    ```

    * Enter a location for your config
        * **Enter (그냥 Enter 치면 c:\Users\사용자이름\.oci 폴더가 기본 폴더로 설정됩니다)**
    * Enter a user OCID
        * **앞에서 획득한 자신의 User OCID**
    * Enter a tenancy OCID
        * **앞에서 획득한 Tenancy OCID**
    * Enter a region
        * **ap-seoul-1**
    * Do you want to generate a new RSA key pair? (SSH Key Pair가 생성됨, 다음 단계에서 OCI에 Public 키를 등록해줌)
        * **y**
    * Enter the location of your private key file:
        * **c:\Users\사용자이름\.oci\oci_api_key.pem**

* c:\Users\사용자이름\.oci 폴더와 config 파일, SSH Key Pair(.pem)가 생성됩니다. SSH Key 중에서 Public Key를 OCI API Key로 등록합니다. 아래와 같이 우측 상단의 사용자 아이콘 클릭 후 사용자 아이디를 클릭 합니다.
    ![](images/oci-get-user-ocid.png)

* 좌측 **API Keys** 메뉴 선택 후 **Add Public Key** 버튼 클릭합니다. **PUBLIC KEY** 영역에 위에서 생성한 키 중에서 oci_api_key_public.pem 파일의 내용을 복사해서 붙여넣기 한 후 **Add** 버튼을 클릭합니다.
    ![](images/oci-add-api-key.png)

* kubeconfig를 생성합니다. 먼저 **Windows PowerShell**을 통해서 다음과 같이 실행해서 oracle 폴더 하위에 .kube 폴더를 생성합니다.
    ```
    # cd $HOME

    # mkdir .kube
    ```
 
* 앞에서 메모한 kubeconfig 생성 명령어를 실행합니다. 
    ```
    # oci ce cluster create-kubeconfig --cluster-id ocid1.cluster.oc1.ap-seoul-1.aaaaaaaaae2dey3fha3diylfgrtgknrugbtdgnjwha2tizddhctdeobrhe4d --file $HOME/.kube/config --region ap-seoul-1
    ```

* 생성된 kubeconfig 파일을 확인합니다.
    ```
    # cd $HOME\.kube

    # type config
    ```

### **STEP 4**: GitHub Repository 생성하기
* https://github.com 에 접속 후 우측 상단의 **Sign in** 클릭 하여 로그인합니다. 좌측 **Create a repository**를 클릭 합니다.

    ![](images/github-create-repo.png)

* **Repository name**을 다음과 같이 입력하고 **Create repository**를 클릭 합니다.
    * Repository name
        * cloud-native-devops-workshop-wercker-oke

    ![](images/github-create-name-repo.png)

* **Import code**를 클릭합니다. 실습을 위해 제공된 Repository의 소스를 가져오기 위한 과정입니다.

    ![](images/github-import-code.png)

* 가져올 GitHub Repository URL을 다음과 같이 입력합니다.
    * Your old repository’s clone URL
        * https://github.com/MangDan/cloud-native-devops-workshop-wercker-oke

    ![](images/github-clone-url-import.png)

* **Import 완료**가 되면 Repository 링크를 클릭해서 확인합니다.
    ![](images/github-import-complete.png)

* **Import 완료**
    ![](images/github-get-repository.png)
    

### **STEP 5**: Wercker 환경 구성하기
* https://app.wercker.com 에 접속 후 **LOG IN WITH GITHUB** 클릭 후 생성한 계정을 통해 로그인한 후 **Create your first application**을 클릭합니다. 
    > Wercker Application은 하나의 Git Repository와 연결되는 파이프라인을 구성하기 위한 단위입니다.

    ![](images/wecker-create-first-app.png)

* **Select SCM**에서 GitHub을 선택합니다.

    ![](images/wercker-create-select-scm.png)

* 앞에서 생성한 GitHub Repository가 보입니다. 선택 후 **Next** 버튼을 클릭 합니다.

    ![](images/github-select-repo.png)

* Git Repository 접속에 필요한 SSH key 설정을 합니다. 실습에서는 SSH key 없이 진행합니다. **Next** 버튼을 클릭 합니다.

    ![](images/wercker-setup-ssh-key.png)

* **Create**버튼을 클릭해서 Wercker Application을 생성합니다.

    ![](images/wercker-app-create.png)

* **Wercker Application 생성**
    ![](images/wercker-app-created.png)


* Wercker Application에서 **Oracle Container Registry** 에 컨테이너 이미지를 푸시하기 위한 설정을 합니다. 상단 탭 메뉴중에서 **Environment**를 선택합니다.

    ![](images/wercker-env.png)

    여기서 필요한 Key와 Value는 다음과 같습니다. 
    1. OCI_AUTH_TOKEN
    2. DOCKER_REGISTRY
    3. DOCKER_USERNAME
    4. DOCKER_REPO
    5. KUBERNETES_MASTER
    6. KUBERNETES_AUTH_TOKEN
    
    > 여기서 KUBERNETES_MASTER와 KUBERNETES_AUTH_TOKEN은 $HOME/.kube/config 파일의 내용을 참조해서 설정합니다.

    1. OCI_AUTH_TOKEN
        * OCI Console 우측 상단의 사용자 아이디를 클릭 후 좌측 **Auth Tokens**를 선택한 후 **Generate Token**을 클릭합니다.
        ![](images/oci-generate-auth-token.png)
        
        DESCRIPTION에 임의로 **Wercker Token**이라고 입력한 후 **Generate Token** 버튼을 클릭합니다.

        ![](images/oci-generate-token-copy.png)
        
        생성된 토큰을 복사한 후 Wercker에 다음과 같이 입력하고 Add 버튼을 클릭합니다.

        **Key:** OCI_AUTH_TOKEN  
        **Value:** 토큰 값 (예. 8K2}JTG96[d82{XXVWRq)

        ![](images/wercker-env-key1.png)
        
    2. DOCKER_REGISTRY
        * Container Registry는 각 리전별로 존재합니다. Registry는 리전키 + ocir.io로 구성되는데, 리전키의 경우는 현재 icn(서울), nrt(도쿄), yyz(토론토), fra(프랑크푸르트), lhr(런던), iad(애쉬번), phx(피닉스)입니다. 여기서는 서울 리전에 있는 Registry를 사용하도록 하겠습니다.

        **Key:** DOCKER_REGISTRY  
        **Value:** icn.ocir.io

    3. DOCKER_USERNAME
        * Docker Username은 OCI 사용자 아이디입니다. OCI Console 우측 상단의 사람 아이콘을 클릭해서 확인할 수 있습니다. 여기에 Tenancy명이 필요합니다. 보통 이름은 다음과 같이 구성됩니다.

        **Key:** DOCKER_USERNAME  
        **Value:** {Tenancy명}/oracleidentitycloudservice/이메일 계정 (예. busanbank1/oracleidentitycloudservice/donghu.kim@oracle.com)

    4. DOCKER_REPO
        * Docker Repository이름으로 Tenancy명 + {레파지토리명}입니다. 레파지토리 이름은 임의 지정합니다.

        **Key:** DOCKER_REPO  
        **Value:** {Tenancy명}/{레파지토리 명} (예. busanbank1/oracle-devops-workshop)

    5. KUBERNETES_MASTER는 .kube/config 파일에서 얻을 수 있습니다. 해당 파일을 편집기로 열어서 MASTER 서버 주소를 복사해서 입력합니다.

        ![](images/oci-oke-kubeconfig-master-server.png)
  
        **Key:** KUBERNETES_MASTER  
        **Value:**: KUBERNETES MASTER URL (예. https://c3donjwgqzd.ap-seoul-1.clusters.oci.oraclecloud.com:6443)

    6. KUBERNETES_AUTH_TOKEN도 마찬가지로 .kube/config 파일에서 얻을 수 있습니다. 해당 파일을 편집기로 열어서 AUTH TOKEN을 복사해서 입력합니다.

        ![](images/oci-oke-kubeconfig-auth-token.png)

        **Key:** KUBERNETES_AUTH_TOKEN  
        **Value:**: KUBERNETES AUTH TOKEN (예. LS0tLS1CRUdJTiBDRVJUSU................)

* Wercker Application 환경 설정을 완료한 모습입니다.
    ![](images/wercker-env-completed.png)

### **STEP 6**: Wercker CI/CD Pipeline 구성하기
> 이미 다 구성된 파일이므로, 내용만 확인합니다.
* 자신의 Git Repository (cloud-native-devops-workshop-wercker-oke)에 접속하면 처음에 생성 시 가져온 파일 중에서 다음 3개의 설정 파일을 확인할 수 있습니다.

    3개의 설정 파일은 다음과 같습니다.
    * wercker.yml
        * Wercker CI/CD의 Pipeline을 구성을 위한 설정 파일
    * kube-helidon-movie-api-mp-config.yml.template
        * helidon-movie-api-mp 서비스를 Kubernetes 환경에 배포하기 위한 설정 파일
    * kube-springboot-movie-people-api-config.yml.template
        * springboot-movie-people-api 서비스를 Kubernetes 환경에 배포하기 위한 설정 파일

* Git Repository에서 wercker.yml 파일을 클릭합니다. 다음과 같은 내용을 볼 수 있습니다. (내용이 길기 때문에 중요한 부분만 요약해서 설명합니다.)
    ```yml
    # 도커 허브에서 아래 이미지를 가져와서 빌드를 위한 컨테이너 환경을 만듭니다.
    box:
      id: jimador/docker-jdk-8-maven-node
      ports:
        - 8080

    # build 파이프라인 입니다. 각 파이프라인 안에는 작업 단위인 step이 포함됩니다. 여기서는 maven을 설치하고, 두개의 서비스를 빌드 및 JUnit 테스트를 거쳐 jar 파일을 만듭니다.
    build:
      steps:
        - script:
        - install-packages:
            packages: maven
            ...
            
    # push-release 파이프라인 입니다. 두 개의 서비스를 컨테이너 이미지화 하여 Oracle Container Registry에 Push를 합니다.
    push-release-1:
      steps:
        - internal/microprofile-docker-push:
            .... helidon(microprofile) 서비스 컨테이너 이미지 생성, 이미지 푸시

    push-release-2:
      steps:
        - internal/springboot-docker-push:
            .... spring-boot 서비스 컨테이너 이미지 생성, 이미지 푸시
    push-release-3:
      steps:
        - script:
            .... 
        - internal/docker-build:
            .... 2개의 서비스를 사용하는 프론트엔드 애플리케이션 컨테이너 이미지 생성, 이미지 푸시
        - internal/docker-push:
            .... 이미지 푸시

    # deploy-to-cluster 파이프라인 입니다. 두 개의 서비스에 대한 Pod를 Kubernetes 노드에 생성하고 서비스로 노출합니다.
    deploy-to-cluster:
      box:
        id: alpine
        cmd: /bin/sh

      steps:
      - bash-template
        
      - kubectl:
          name: delete secret
          ... Wercker에서 Docker Registry 접속을 위한 Secret이 존재할 경우 삭제

      - kubectl:
          name: create secret
          ... Wercker에서 Docker Registry 접속을 위한 Secret을 다시 생성

      - script:
          name: "Visualise Kubernetes config"
          code: cat kube-helidon-movie-api-mp-config.yml

      - kubectl:
          name: deploy helidon-movie-api-mp to kubernetes
          ... helidon-movie-api-mp 서비스 Pod 생성
    
      - script:
          name: "Visualise Kubernetes config"
          code: cat kube-springboot-movie-people-api-config.yml

      - kubectl:
          name: deploy springboot-movie-people-api to kubernetes
          ... springboot-movie-people-api 서비스 Pod 생성
      - script:
          name: "Visualise Kubernetes config"
          code: cat kube-jet-movie-msa-ui-config.yml

      - kubectl:
          name: deploy jet-movie-msa-ui to kubernetes
          ... jet-movie-msa-ui 서비스 Pod 생성
    ```

* 위 wercker.yml에는 다음과 같이 4개의 파이프라인을 임의로 지정했습니다. 
    * build
        * 프로젝트를 빌드/테스트/패키징(jar) 합니다.
    * push-release-1
        * Helidon (Microprofile) 프로젝트를 컨테이너 이미지화 하여 Container Registry에 푸시합니다.
    * push-release-2
        * Spring Boot 프로젝트를 컨테이너 이미지화 하여 Container Registry에 푸시합니다.
    * deploy-to-cluster
        * 컨테이너 이미지를 가져와서(pull) 쿠버네티스 노드에 Pod를 생성하고 서비스를 시작합니다.

* wercker.yml 파일에 정의한 파이프라인을 Wercker에 등록하고 Workflow를 구성합니다. 먼저 Wercker (https://app.wercker.com)에 접속한 후 **Workflows**탭을 선택합니다. 중간에 보면 **build** 파이프라인은 디폴트로 만들어져 있습니다.(변경 가능합니다.) 아래 **Add new Pipeline** 버튼을 클릭합니다.

    ![](images/wercker-workflows-add-pipeline.png)
    
* **build**는 이미 생성되어 있기 때문에 두 번째 파이프라인인 **push-release-1**을 입력하고 **Create**버튼을 클릭합니다. 

    ![](images/wercker-create-pipeline-push-1.png)

* 다시 상단 **Workflows**탭을 클릭 합니다. 동일하게 세 번째 파이프라인인 **push-release-2**를 입력하고 **Create**버튼을 클릭합니다.

    ![](images/wercker-create-pipeline-push-2.png)

* 동일하게 네 번째 파이프라인인 **push-release-3**를 입력하고 **Create**버튼을 클릭합니다.

    ![](images/wercker-create-pipeline-push-3.png)

* 동일하게 다섯 번째 파이프라인인 **deploy-to-cluster**를 입력하고 **Create**버튼을 클릭합니다.

    ![](images/wercker-create-pipeline-2.png)

* 다시 상단 **Workflows**탭을 클릭한 후 **Workflow** 구성을 위해 **build** 파이프라인 옆 **+** 아이콘을 클릭 합니다.

    ![](images/wercker-create-workflow-add.png)

* 맨 아래 **pipeline**을 **push-release-1** 선택 후 **Add**버튼을 클릭 합니다.

    ![](images/wercker-workflow-add-1.png)

* **push-release-1** 파이프라인 옆 **+** 아이콘을 클릭 합니다.

    ![](images/wercker-workflow-add-2.png)

* **push-release-2** 선택 후 **Add**버튼을 클릭 합니다.

    ![](images/wercker-workflow-add-3.png)

* **push-release-2** 파이프라인 옆 **+** 아이콘을 클릭 합니다.

    ![](images/wercker-workflow-add-4.png)

* **push-release-3** 선택 후 **Add**버튼을 클릭 합니다.

    ![](images/wercker-workflow-add-5.png)

* **push-release-3** 파이프라인 옆 **+** 아이콘을 클릭 합니다.

    ![](images/wercker-workflow-add-6.png)

* **deploy-to-cluster** 선택 후 **Add**버튼을 클릭 합니다.

    ![](images/wercker-workflow-add-7.png)

* 완성된 Wercker Workflow 모습입니다.
    ![](images/wercker-workflow-complete.png)

### **STEP 7**: Wercker 파이프라인 실행하기
* 상단 **Runs**탭을 선택합니다. 아래 **trigger a build now.** 링크를 클릭합니다. 최초 파이프라인 실행할 경우만 이 버튼으로 실행하며, 이후부터는 GitHub의 변경사항이 발생할 경우 자동으로 빌드 파이프라인이 실행됩니다.
    ![](images/wercker-first-build.png)

* **Build** 파이프라인이 시작되었습니다.
    ![](images/wercker-start-workflow-pipeline.png)


* 모든 파이프라인 (빌드/테스트/패키징 --> 이미지 생성 및 레지스트리 등록 --> 쿠버네티스 파드 컨테이너 생성)이 오류 없이 정상적으로 완료되었습니다. 
    ![](images/wercker-workflow-pipeline-run-completed.png)

  

### **STEP 8**: Oracle Container Registry (OCIR) 확인하기
* OCI에 접속 (https://console.ap-seoul-1.oraclecloud.com?tenant=busanbank1) 후 좌측 **Developer Services** > **Registry (OCIR)** 클릭 합니다.
    ![](images/oci-menu-ocir.png)

* OCIR에 이미지가 등록되었습니다. 현재 Helidon(Microprofile)과 Spring Boot으로 개발된 서비스가 이미지로 등록된 것을 확인할 수 있습니다.
    ![](images/oci-ocir-repository-1.png)

### **STEP 9**: Oracle Kubernetes Engine (OKE) 에 생성(배포)된 Pod와 Service 확인하기
* **Windows PowerShell**을 열고 다음과 같이 명령어를 실행합니다.

    ```
    # kubectl get all
    ```

* 다음과 같이 **Running**상태의 두 개의 서비스와 서비스의 **External IP**를 확인할 수 있습니다.
    ![](images/kubectl-get-all.png)



### **STEP 10**: 서비스 확인하기
* 브라우저를 열고 다음과 같이 확인한 External IP로 접속합니다.
    * Helidon(Microprofile) Movie 서비스 확인
        ```
        http://{External_IP}:30000/api/search/v1/movies
        ```

    * Spring Boot Movie People 서비스 확인
        ```
        http://{External_IP}:31000/moviepeople
        ```
    * Movie Web Page
        ```
        http://{External_IP}
        ```



        추가해야 할 내용들..
        1. MS Powershell 화면 캡쳐
        2. JET 관련 부분 GitHub 이미지 확인
        3. Wercker Pipeline에 jet 관련된 부분 추가 (push-release-3)
        4. JET에 MoviePeople 추가할 지...