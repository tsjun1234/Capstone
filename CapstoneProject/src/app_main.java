
public class app_main {
	public static void main(String [] args) {
		App m=new App();
	}
}

//TODO(10/12)
//현재 작업중
//태그 생성시 position을 선택(기본 static)
//다만 사용자가 style태그를 따로 추가시 해당 속성값까지 포현해 값을 다시 조정해야함
//setTagDialog에서 style태그에 대한것만 따로 조치를 취해야할듯
//해결해야하는것
//태그를 그래픽화, 선택한 태그를 기준으로 추가하는 방안(일부 구현)
//1.파일 모듈
//1.1)파일 저장 HTML만 구현, css, js기능 미구현
//1.2)파일 읽기 HTML만 구현, css, js기능 미구현
//1.3)작업 종료시 안내창 미구현
//1.4)작업 시작시 최근 작업물 자동 불러오기 X -> 설정모듈처럼 따로 사용자가 지정한것들을 저장해야할 필요성(편의성, 안해도 됨)
//2)HTML
//https://www.w3schools.com/tags기준으로 작업
//2.1)태그 모양 미구현 -> 현재 선으로만 표시
//2.2)태그 선택기능 미구현 -> 현재 태그 생성만 가능(수정은 불가)
//2.3)태그의 옵션기능 미구현 -> 현재 위치도 설정 불가능(태그별 구체화 하기 전 위치 지정이라도 가능하게 만들기)
//2.4)태그 안의 태그 삽입기능 미구현
//2.5)태그간의 위치를 absolute만 가능, relative, fixed등 설계해야함
//2.6)파일 읽은 것을 바탕으로 화면에 재구성 해야함, 위치좌표와 크기등이 있어야하므로 태그개선이 먼저 필요
//3)CSS모듈
//3.1)CSS파일을 직접 작성 or HTML태그 선택시 해당 옵션선택기능
//4)JS