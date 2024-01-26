USE dbdbdeep;


-- 송재원의 학생의 1/24 출결 상황 보여주기
SELECT s.std_name AS '학생이름',r.date AS '날짜', r.status AS '출결'
FROM student s
JOIN rollbook r ON(s.std_id=r.std_id)
WHERE s.std_name='송재원' AND r.date='2024-01-24';


-- 면접 인원에서 면접 점수를 통해 상위 30명을 뽑아서 보여주기
SELECT ap_name AS '면접자',itv_score AS '면접 점수'
FROM interview i
JOIN applier a ON (i.ap_id=a.ap_id)
ORDER BY 'itv_score' DESC LIMIT 30;


-- 수업 첫 날, 하지민 학생이 자신의 강의실 호수를 몰라서 지나가던 매니저님에게 물어보았을 경우 매니저님이 강의실 호수를 찾는 케이스
SELECT s.std_name AS '학생 이름', g.class_num AS '강의실 호수' 
FROM gen g
JOIN student s ON (g.gen_id=s.gen_id) 
WHERE s.std_name='하지민';


-- 1기의 과목별 성적 내림차순 정렬
SELECT sj.sub_name AS '과목 이름', st.std_name AS '학생 이름', sc.test_score AS '시험 점수'
FROM score sc
JOIN student st on (sc.std_id=st.std_id)
JOIN `subject` sj ON (sc.sub_id=sj.sub_id) 
WHERE st.gen_id=1
ORDER BY sc.sub_id ASC, sc.test_score DESC;


-- 빈 테이블 만들어두고 거기에 매 기수 성적 총합 1~3등의 정보를 넣는 과정과 목록 보여주기
-- 일단 1기 1~3등 뽑는 쿼리만
SELECT st.std_name AS '학생 이름', sum(sc.test_score) AS '성적 총합'
FROM student st
JOIN score sc ON (st.std_id=sc.std_id)
WHERE st.gen_id=1
GROUP BY st.std_id
ORDER BY sum(sc.test_score) DESC LIMIT 3;


-- 정부에서 지원금이 나왔다. 저번달  학생별 출결에 비례하는 지원금 목록을 만들어 보자
SELECT  a.학번
		  , b.std_name
		  , CONCAT(TRUNCATE(((a.총교육일수- (a.결석 + TRUNCATE((a.조퇴+a.지각+a.외출)/3,0)))/a.총교육일수)*100,0),'%') AS '출석율'
		  , case
		  		when TRUNCATE(((a.총교육일수- (a.결석 + TRUNCATE((a.조퇴+a.지각+a.외출)/3,0)))/a.총교육일수)*100,0) >= 80
		  		then (a.총교육일수- (a.결석 + TRUNCATE((a.조퇴+a.지각+a.외출)/3,0)))*5800 + 200000
			 	ELSE 0 end AS '지원금산정'
  FROM  (
			SELECT  std_id AS '학번'
					  ,count(case when STATUS = '출석' then 1 ELSE null END) AS '출석'
					  ,count(case when STATUS = '결석' then 1 ELSE null END) AS '결석'
					  ,count(case when STATUS = '조퇴' then 1 ELSE null END) AS '조퇴'
					  ,count(case when STATUS = '지각' then 1 ELSE null END) AS '지각'
					  ,count(case when STATUS = '외출' then 1 ELSE null END) AS '외출'
					  ,COUNT(std_id) AS '총교육일수'       
			  FROM  rollbook
			 WHERE  MONTH(rb_date) = 12
			 group
			    BY  std_id
		  ) AS a
  JOIN  student AS b
    ON  a.학번 = b.std_id
		  ; 


-- 오늘 기준 출결이 80퍼센트 미만인 학생 목록 보여주기
SELECT  a.학번
		  , b.std_name
		  , CONCAT(TRUNCATE(((a.총교육일수- (a.결석 + TRUNCATE((a.조퇴+a.지각+a.외출)/3,0)))/a.총교육일수)*100,0),'%') AS '출석율'
  FROM  (
			SELECT  std_id AS '학번'
					  ,count(case when STATUS = '출석' then 1 ELSE null END) AS '출석'
					  ,count(case when STATUS = '결석' then 1 ELSE null END) AS '결석'
					  ,count(case when STATUS = '조퇴' then 1 ELSE null END) AS '조퇴'
					  ,count(case when STATUS = '지각' then 1 ELSE null END) AS '지각'
					  ,count(case when STATUS = '외출' then 1 ELSE null END) AS '외출'
					  ,COUNT(std_id) AS '총교육일수'       
			  FROM  rollbook
			 group
			    BY  std_id
		  ) AS a
  JOIN  student AS b
    ON  a.학번 = b.std_id
 WHERE  TRUNCATE(((a.출석- (a.결석 + TRUNCATE((a.조퇴+a.지각+a.외출)/3,0)))/a.총교육일수)*100,0) < 80
		  ; 



-- 매 기수마다 성적 총합 1~3등 학생들은 우수 수강생으로 선발되어 한화시스템 인턴쉽 기회가 제공되며 해당 데이터는 테이블로 따로 관리된다.
-- 1기와 2기 우수수강생 데이터를 차례로 삽입해보자.  

-- 1기 우수수강생 삽입
INSERT INTO beststudent
(SELECT st.std_id, SUM(sc.test_score) AS score_sum
FROM student st
JOIN score sc ON (st.std_id=sc.std_id)
WHERE st.gen_id=1
GROUP BY st.std_id
ORDER BY score_sum DESC
LIMIT 3);

-- 2기 우수수강생 삽입
INSERT INTO beststudent
(SELECT st.std_id, SUM(sc.test_score) AS score_sum
FROM student st
JOIN score sc ON (st.std_id=sc.std_id)
WHERE st.gen_id=2
GROUP BY st.std_id
ORDER BY score_sum DESC
LIMIT 3);
SELECT *
FROM beststudent ;

-- 매 기수마다 1과목 시험 성적 상위 1~5등 학생들은 기숙사 우선 입주권이 부여된다.
-- 기수, 1과목 성적 점수, 기숙사 지원 여부를 고려하여 인원을 선발한 후 해당 인원의 std_id는 dormitory 테이블로 관리한다 
-- 1기와 2기 기숙사 데이터를 차례로 삽입해보자.  

-- 1기 1과목 성적 상위  5명 정보 조회 쿼리
SELECT  a.std_id
		  , a.gen_id
		  , a.std_vol
		  , b.test_score
		  , b.sub_id
  FROM  student AS a
  JOIN  score AS b
    ON  a.std_id = b.std_id
 WHERE  a.gen_id = 1
 		  AND b.sub_id = 1
 		  AND a.std_vol = 'y'
 order
    BY  b.test_score DESC
 LIMIT  5
 		;

-- dormitory table에 1기 1과목 성적 상위 5명 std_id 삽입쿼리
INSERT  
  INTO  dormitory(std_id)

			SELECT  a.std_id
			  FROM  student AS a
			  JOIN  score AS b
			    ON  a.std_id = b.std_id
			 WHERE  a.gen_id = 1
			 		  AND b.sub_id = 1
			 		  AND a.std_vol = 'y'
			 order
			    BY  b.test_score DESC
			 LIMIT  5
	    ;

-- 2기 1과목 성적 상위  5명 정보 조회 쿼리
SELECT  a.std_id
		  , a.gen_id
		  , a.std_vol
		  , b.test_score
		  , b.sub_id
  FROM  student AS a
  JOIN  score AS b
    ON  a.std_id = b.std_id
 WHERE  a.gen_id = 2
 		  AND b.sub_id = 1
 		  AND a.std_vol = 'y'
 order
    BY  b.test_score DESC
 LIMIT  5
 		;

-- dormitory table에 2기 1과목 성적 상위 5명 std_id 삽입쿼리
INSERT  
  INTO  dormitory(std_id)

			SELECT  a.std_id
			  FROM  student AS a
			  JOIN  score AS b
			    ON  a.std_id = b.std_id
			 WHERE  a.gen_id = 2
			 		  AND b.sub_id = 1
			 		  AND a.std_vol = 'y'
			 order
			    BY  b.test_score DESC
			 LIMIT  5
		;