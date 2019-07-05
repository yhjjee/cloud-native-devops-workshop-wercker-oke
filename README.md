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
* Git 설치

***

## **STEP 1**:  Setup

### GitHub 계정 생성
* https://github.com 에 접속해서 우측 상단 **Sign up**을 클릭합니다.
![](images/github_signup.png)

* 다음 내용을 입력하고 **Create an account** 클릭해서 계정 생성합니다. 검증 메일이 발송되기 때문에 정확한 이메일을 입력해야 합니다. GitHub으로 부터 수신받은 이메일에서 **Verify email address**를 클릭해서 계정 생성을 완료합니다.

    ![](images/github_create_account.png)

    ![](images/github_verify_email.png)

* 생성한 계정으로 Sign in 합니다.

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

### Git 설치
* Git 설치는 아래 블로그를 참고하여 설치합니다.  
    * https://boogong.tistory.com/58

### Git Repository Clone
* Git 설치가 완료되면 실습을 위해 제공된 GitHub Repository를 로컬에 다운로드 받는 Clone 작업을 합니다. 먼저, Windows 좌측 하단의 검색 버튼을 클릭하고 **PowerShell**을 입력한 후 **Windows PowerShell**을 실행합니다.

    <img src="images/windows-search-powershell.png" width="50%">

    ![](images/windows-powershell.png)

    [Image Source: https://itexplorer.tistory.com/40]

* **Windows PowerShell**에서 다음과 같이 입력해서 C 드라이브에 실습을 위한 Git Repository를 가져옵니다.
    > #은 구분 표시로 입력하지 않습니다.
    ```
    # cd c:\
    # git clone https://github.com/MangDan/cloud-native-devops-workshop-wercker-oke.git
    ```

### **STEP 2**: OCI에서 Kubernetes Cluster 생성하기
* 먼저 OCI에 로그인합니다. 아래 URL을 통해서 Seoul Region으로 접속합니다.
    > tenancy 명은 처음 Oracle Cloud Subscription 시에 명명한 이름을 사용합니다.

    * https://console.ap-seoul-1.oraclecloud.com/?tenant={tenancy명}

    > 두 가지 로그인 타입이 있습니다. OCI 전용 계정이 있으며, IDCS라는 계정 관리를 위한 클라우드 서비스와 연동 (Federation)해서 사용하는 계정이 있습니다. 생성한 계정 타입을 확인 후 관련 계정으로 로그인 합니다.

    ![](images/oci-login.png)

    좌측 상단의 햄버거 모양의 아이콘을 클릭합니다.
    ![](images/oci-console.png)

* 좌측 메뉴 중 **Developer Services** > **Container Clusters (OKE)** 선택합니다.

    ![](images/oci-menu-oke.png)

* OKE Cluster를 생성 할 Compartment를 선택합니다.
    > Compartment는 OCI에서 관리하는 리소스들을 그룹으로 묶어서 관리하기 위해 제공되는 기능입니다. 일반적으로 팀 단위로 리소스(Compute, Network, Storage등)를 관리하기 위한 목적으로 사용됩니다. Compartment는 구성하기 나름이기 때문에 아래 스크린샷과 다를 수 있습니다.

    ![](images/oci-create-oke-cluster-compartment.png)

* OKE Cluster 생성을 위해 **Create Cluster**버튼을 클릭 합니다.

    ![](images/oci-create-oke-cluster.png)

* 다음과 같이 입력합니다.  Virtual Machine Gen2, 1 OCPU에 생성된 네트워크 서브넷당 한개의 쿠버네티스 노드를 생성합니다.
    > Compute Shape의 의미는 다음과 같습니다. VM.Standard2.1은 Virtual Machine Gen2, 1 OCPU를 의미합니다.

    * NAME: oke-cluster1
    * KUBERNETES VERSION: v1.12.7
    * QUICK CREATE: CHECK
    * SHAPE: VM.Standard2.1
    * QUANTITY PER SUBNET: 1

    <img src="images/oci-create-oke-cluster-creation.png" width="50%">

* 정상적으로 생성이 되면 생성된 노드의 상태가 ACTIVE가 됩니다.
  
  **생성 진행 과정**  
    ![](images/oci-oke-cluster-created.png)
    
  **ACTIVE 상태의 노드**  
    <img src="images/oci-created-oke-cluster.png" width="50%">

### **STEP 3**: kubectl 와 oci-cli 설치하기
* 클라이언트에서 OKE 접속을 위해서는 kubeconfig 파일을 생성해야 합니다. kubeconfig 파일을 얻는 과정은 OCI의 OKE Cluster 화면에서 Access **Kubeconfig** 버튼을 클릭하면 확인할 수 있습니다.

    **Access Kubeconfig 버튼** 
    ![](images/oci-oke-access-kubeconfig-1.png)

    **Kubeconfig를 얻기 위한 과정**
    ![](images/oci-oke-access-kubeconfig-2.png)

* Kubeconfig를 얻기 위해서는 먼저 **oci-cli**를 설치합니다. Windows의 **Windows PowerShell**을 열고 (Windows 좌측 아래 검색 버튼 클릭 후 PowerShell 입력) 다음과 같이 입력해서 oci-cli를 설치합니다.

    > oci-cli 설치를 위해 Python이 우선 설치됩니다.
    ```
    # Set-ExecutionPolicy RemoteSigned

    # powershell -NoProfile -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://raw.githubusercontent.com/oracle/oci-cli/master/scripts/install/install.ps1'))"
    ```

* Python 설치가 완료되면 oci-cli 설치 경로를 설정합니다. 경로는 기본 경로(c:\Users\사용자명\)에 설치해도 되지만, Windows의 경우 사용자명에 공백이 있을 경우 설치가 되지 않습니다. 본 실습에서는 c:\oracle을 기본 경로로 합니다.
    ```
    1. c:\oracle\oci-cli
    2. c:\oracle\bin
    3. c:\oracle\bin\oci-cli-scripts
    4. 추가 패키지 설치 여부 (설치 없이 엔터 치고 넘어갑니다)
    ```

* 설치가 완료되면 **PowerShell**을 다시 시작한 후 oci-cli 설치를 확인합니다.
    ```
    # oci -v
    ```
* oci-cli 설치가 완료되면 Oracle Cloud Infrastructure와 연결을 위한 설정을 합니다. 이 때 필요한 정보는 다음과 같습니다.
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

    3. SSH Key Pair (Public/Private Key)
    Oracle Cloud Infrastructure에 접속하기 위해서 기본적으로 SSH Key Pair가 필요합니다. macOS나 리눅스 환경에서는 ssh-keygen을 통해 생성하고 Windows 환경에서는 putty를 활용해서 생성합니다. 하지만, 여기서는 이미 생성된 SSH Key Pair를 사용합니다. GitHub에서 Clone한 파일 폴더의 keys 폴더내의 다음 파일을 복사해서 c:\oracle 폴더로 붙여넣기 합니다. **PowerShell** 혹은 Windows탐색기를 사용합니다.

        **Windows PowerShell을 사용하는 경우**
        ```
        # cd c:\cloud-native-devops-workshop-wercker-oke\keys

        # cp * c:\oracle
        ```
    

 






### **STEP 4**: Wercker 환경 구성하기

### **STEP 5**: Wercker와 Kubernetes 설정 파일 구성하기 (옵션: Blue/Green Deployment)

### **STEP 6**: Wercker CI/CD Pipeline 구성하기

### **STEP 7**: 애플리케이션을 GitHub에 커밋하기

### **STEP 8**: CI/CD 파이프라인 진행 모니터링 하기

### **STEP 9**: Kubernetes 배포된 Pod와 Service 확인

### **STEP 10**: 최종 배포된 애플리케이션 확인