-- GIKBTI 옵션 초기 데이터
-- 중복 방지: 옵션 테이블이 비어있을 때만 전체 삽입

-- 옵션이 하나도 없을 때만 전체 삽입
INSERT INTO gikbti_option (gikbti_question_id, text, is_true, display_order, created_at, updated_at)
SELECT 1, '12시 이전에 자요', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 1, '새벽 2-3시에 자요', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 2, '8시 쯤 일어나서 하루를 시작해요', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 2, '나에게 오전은 없다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 3, '낮시간에 일어나서 작업을 해요', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 3, '밤에 작업을 많이 해요', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 4, '이르거나 늦어도 괜찮다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 4, '잘 때 그러면 힘들다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 5, '바로 치운다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 5, '한번에 몰아서 치운다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 6, '항상 정리된 편', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 6, '더러운편이다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 7, '1일 / 3일', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 7, '1주 / 1개월', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 8, '참을 수 없다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 8, '상관없다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 9, '나가서 했으면 좋겠다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 9, '상관없다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 10, '힘들다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 10, '가능하다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 11, '거슬린다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 11, '신경 안쓴다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 12, '바로 끈다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 12, '잘 못들어서 여러번 울린다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 13, '밥도 먹고 친해지고 싶다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 13, '거리를 두고 싶다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 14, '미리 말하면 괜찮다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 14, '싫다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 15, '잘때만 들어간다', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 15, '수업시간 빼고 많이 들어간다', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 16, '절대 안됨!! 내 공간 지켜', true, 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1)
UNION ALL
SELECT 16, '뭐 어때', false, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option LIMIT 1);
