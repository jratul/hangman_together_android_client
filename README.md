# hangman_together_android_client

## 프로젝트 개요
본 repository는 SW MAESTRO 7기 모바일 과제를 위한
안드로이드 클라이언트 repository입니다.

친구들과 점수 경쟁을 할 수 있는 가벼운 행맨 게임입니다.


## 과제 명세
### 필수 사항
1. **앱(게임)과 서버 통신하는 로직이 포함되어야함.**    
    Retrofit을 이용하여 안드로이드 클라이언트에서 서버에 request를 보내고 결과 값을 받습니다.

2. **클라이언트 게임은 게임 첫 설치 시간, 접속 시간, 접속 후 게임 종료 시간, 마지막 플레이 시간 등이 DB에 저장되어야 함.**  
  앱에 로그인을 하면 접속 시간이 DB에 기록되고, 게임을 시작하면 마지막 플레이 시간이 갱신됩니다.

3. **페이스북, 트위터, 이메일 등 타 서비스 연계가 1개 이상 되어야 함.**  
  페이스북 로그인 연동하였습니다.

4. **앱스토어에 업로드 되어야 함.**  
  플레이스토어 URL : https://play.google.com/store/apps/details?id=com.swmaestro.hangman_together

5. **서버 프로그램이 클라우드나 호스팅이 되는 서버에 배포가 되어야 함.**  
  서버는 AWS를 통해서 서비스하고 있습니다.

6. **데이터는 DB에 저장되어 SQL을 통해서 조회 및 추가가 가능해야 함.**  
  mySQL 통해서 DB를 구축했습니다.

7. **백엔드는 BaaS나 클라우드 제한 없으나 웹서비스 형태이어야 함.**  
  nginx를 사용했습니다.
  
### 가점 사항
1. **클라우드 이용 시 가점**  
  AWS 사용했습니다.

2. **친구 추가, 랭킹, 선물하기 등의 기능 추가 시 가점**  
  닉네임을 통해 친구를 추가할 수 있고, 행맨 게임을 플레이해서 이길 경우, 점수가 쌓입니다. 이 점수를 통한 랭킹을 확인할 수 있습니다.
  행맨 게임을 플레이하기 위해서는 하루에 5개씩 충전되는 '캔디'를 사용해야하는데, 하루에 1번 친구에게 캔디를 선물할 수 있습니다.

3. **Restful API 형태 설계 적용 시 가점**  
  CRUD 메서드를 사용하고, 어떤 플랫폼이든 응답 가능합니다.

4. **PUSH 구현 되었으면 가점**  
  GCM 서비스를 사용할 수 있도록 하였고, 로그인 시 GCM 서비스를 위한 instance id를 받아와서 서버에 저장하도록 했습니다.

5. **~~안드로이드와 아이폰 둘다 대응 시 가점~~**  

6. **~~DB와 연동되는 별도의 CMS(관리시스템)을 구축 시 가점~~**  

## 프로젝트 구조
**app package**  

        **common** : savePreference, 현재 시각 가져오기 등 자주 쓰이는 static 메서드, 전역 변수 등 관리 
        
        **rest** : Retrofit으로 서버와 통신하기 위한 부분  
        
                **addfriend** : 친구 추가  
                **checkid** : 로그인 시 계정이 있는지 확인  
                **endgame** : 게임 종료 시 점수 갱신  
                **getfriend** : 친구 목록 가져오기  
                **getstash** : 캔디 보관함 목록 가져오기  
                **givecandy** : 친구에게 캔디 주기  
                **home** : 홈 화면을 위한 내 점수, 내 캔디, 랭킹 목록 가져오기  
                **join** : 가입하기  
                **login** : 로그인하기  
                **startgame** : 게임 시작 시, 단어를 가져오고 캔디 소모  
                **takeallcandy** : 보관함에 있는 모든 캔디를 받기  
                **takecandy** : 보관함에 있는 캔디 하나 받기  
                
        **ui** : 안드로이드 ui, GCM 관련, Application  
                **base** : 툴바를 사용하는 액티비티들이 상속할 수 있는 BaseActivity  
                **friend** : 친구 목록 Fragment, 친구 목록 Adapter, 친구 목록 데이터 모델  
                **game** : 행맨 게임 플레이 Activity  
                **home** : 홈 Fragment, 랭킹 목록 Adpater, 랭킹 목록 데이터 모델  
                **intro** : 로그인 화면  
                **main** : 메인 View Pager (홈 화면, 친구 목록, 보관함으로 구성), view pager adapter  
                **splash** : Splash 화면  
                **stash** : 보관함 Fragment, 보관함 목록 Adpater, 보관함 목록 데이터 모델  
    
## 앱의 흐름
1. 앱을 실행하면 스플래시 화면이 실행되는데 이 때 사용자가 로그인을 한 상태인지 확인합니다.

2. 이전에 로그인을 한 상태이면 바로 메인 화면으로 들어가고 그렇지 않거나 로그인에 실패하면  
  인트로 화면에 들어갑니다. 인트로 화면에서 로그인 시도를 했을 때 기기의 전화번호와 일치하는 아이디가 존재하면  
  해당 아이디로 접속이 되고, 그렇지 않으면 아이디를 생성하기 위해 닉네임을 입력받습니다.

3. 메인 페이지는 홈 화면, 친구 목록, 보관함으로 구성되어 있습니다.

4. 홈 화면에서는 내 캔디, 내 누적 점수, 랭킹 TOP 10을 확인할 수 있고, 게임을 시작할 수 있습니다.

5. 친구 목록에서는 내 친구 목록 확인, 친구 추가, 친구에게 캔디 주기를 할 수 있습니다.

6. 보관함에서는 친구에게 받은 캔디를 가져올 수 있습니다.

7. 게임을 시작하면 캔디가 하나 줄어들고, 스펠 하나를 맞출 때마다 3점을 얻고, 하나 틀릴 때마다 1점을 잃습니다.  
  게임이 종료된 후 누적 점수에 반영이 됩니다.

8. 매일 자정에 캔디가 5개 이하인 유저들은 캔디가 5개로 충전이 되고, 캔디를 선물할 수 있게 됩니다.

## 개선해야 할 점
1. 서버와의 통신에서 전화 번호를 사용하는데 이 보다는 유저 인덱스를 한 번 받아오는 것이 쿼리문을 낭비하지 않을 것 같습니다.

2. Retrofit을 처음 사용해보고 백 엔드 작성 경험이 없어서 이를 사용하는데 많은 시간을 소비했는데 모듈화를 하지 못해서  
  서버와 통신하는 부분의 코드가 많이 반복됩니다. 

3. 게임 액티비티에서 코드 간결화가 시급합니다.

4. 프로가드 적용을 위한 룰 작성이 서툴러서 실패했습니다.

5. 통신 응답 처리가 미약합니다. ex. 서버와의 연결이 불안정할 때 등

6. 부족한 기획 탓에 코드 재사용성이 많이 떨어집니다.


읽어주셔서 감사합니다. 서버에 관한 것은 'hangman_together_server' repository의 README파일을 참고해주세요.
