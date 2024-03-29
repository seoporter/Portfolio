package kr.ws.travel.paging;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import kr.ws.travel.board.BoardVO;

// 1페이지 분량의 정보를 저장할 클래스
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({BoardVO.class})
public class Paging<T> implements Serializable{

	private static final long serialVersionUID = -2871491755481021815L;

	@XmlElementWrapper(name="content_lists")
	private List<T> lists; // 글목록
	
	@XmlElement
	private int totalCount; 	// 전체 갯수
	@XmlElement
	private int currentPage; 	// 현재 페이지
	@XmlElement
	private int pageSize;		// 페이지당 글 개수
	@XmlElement
	private int blockSize;		// 아래 페이지번호 표시 개수
	
	@XmlElement
	private int totalPage;		// 전체 페이지 수
	@XmlElement
	private int startNo;		// 시작 글번호
	@XmlElement
	private int endNo;			// 끝 글번호(이것은 Oracle에서만 사용)
	@XmlElement
	private int startPage;		// 시작 페이지번호
	@XmlElement
	private int endPage;		// 끝 페이지번호
	

	public Paging() {
		
	}
	
	public Paging(int totalCount, int currentPage, int pageSize, int blockSize) {
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.blockSize = blockSize;
		calc();
	}
	
	// 나머지 멤버들을 계산할 메서드
	private void calc() {
		// 글이 있을때만 계산
		if(totalCount>0) {
			// 입력된값의 유효성 검사
			if(pageSize<1) pageSize = 10;
			if(blockSize<2) blockSize = 10;

			totalPage = (totalCount-1)/pageSize + 1;
			if(currentPage<0 || currentPage>totalPage) currentPage=1;
			startNo = (currentPage-1) * pageSize + 1; // Mysql의 경우 +1을 뺀다.
			// 마리아디비는 사용하지 않는다. 오라클에서만 사용한다.
			endNo = startNo + pageSize -1;
			if(endNo>totalCount) endNo = totalCount;
			
			startPage = (currentPage-1)/blockSize * blockSize + 1;
			endPage = startPage + blockSize -1;
			if(endPage>totalPage) endPage = totalPage;
		}
	}
	public List<T> getLists() {
		return lists;
	}
	public void setLists(List<T> lists) {
		this.lists = lists;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public int getBlockSize() {
		return blockSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public int getStartNo() {
		return startNo;
	}
	public int getEndNo() {
		return endNo;
	}
	public int getStartPage() {
		return startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	
	// 페이지 정보를 출력해주는 메서드
	public String getPageInfo() {
		String message = "전체 : " + totalCount + "개";
		if(totalCount>0) {
			message += "(" + currentPage + "/" + totalPage + "Page)";
		}
		return message;
	}
	
	// 페이지 번호를 출력해주는 메서드
	public String  getPageList() {
		String message="";
		// 이전 표시
		if(startPage>1) {
			message += String.format("[<a href='?p=%d&s=%d&b=%d'>이전</a>] ", startPage-1, pageSize, blockSize);
		}
		// 페이지 목록
		for(int i=startPage;i<=endPage;i++) {
			if(i==currentPage) {
				message += "[" + i + "] ";
			}else {
				message += String.format("[<a href='?p=%d&s=%d&b=%d'>%d</a>] ", i, pageSize, blockSize, i);
			}
		}
		// 다음 표시
		if(endPage<totalPage) {
			message += String.format("[<a href='?p=%d&s=%d&b=%d'>다음</a>] ", endPage+1, pageSize, blockSize);
		}
		return message;
	}
	
	// 페이지 번호를 출력해주는 메서드
	public String  getPageList2() {
		String message="";
		// 이전 표시
		if(startPage>1) {
			message += "[<span style=\"cursor:pointer;color:blue;\" onclick=\"post_to_url('list', {'p':'" +(startPage-1)+"','s':'" + pageSize + "','b':'" + blockSize+"'});\">이전</span>] ";
		}
		// 페이지 목록
		for(int i=startPage;i<=endPage;i++) {
			if(i==currentPage) {
				message += "[<span style=\"cursor:pointer;color:black;\">" + i + "</span>]";
			}else {
				message += "[<span style=\"cursor:pointer;color:blue;\" onclick=\"post_to_url('list', {'p':'" +i+"','s':'" + pageSize + "','b':'" + blockSize+"'});\">"+i+"</span>] ";
			}
		}
		// 다음 표시
		if(endPage<totalPage) {
			message += "[<span style=\"cursor:pointer;color:blue;\" onclick=\"post_to_url('list', {'p':'" +(endPage+1)+"','s':'" + pageSize + "','b':'" + blockSize+"'});\">다음</span>] ";
		}
		return message;
	}
	@Override
	public String toString() {
		return "Paging [lists=" + lists + ", totalCount=" + totalCount + ", currentPage=" + currentPage + ", pageSize="
				+ pageSize + ", blockSize=" + blockSize + ", totalPage=" + totalPage + ", startNo=" + startNo
				+ ", endNo=" + endNo + ", startPage=" + startPage + ", endPage=" + endPage + "]";
	}
}
