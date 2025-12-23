-- GIKBTI 질문 초기 데이터 (가중치 포함)
-- 중복 방지: 질문 테이블이 비어있을 때만 전체 삽입
-- 각 카테고리의 총 가중치 합: 10점

-- 아침형/저녁형 (MORNING_EVENING) - 총 10점
INSERT INTO gikbti_question (category, text, is_active, display_order, weight, created_at, updated_at)
SELECT 'MORNING_EVENING', '수면 시간은?', true, 1, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'MORNING_EVENING', '기상 시간은?', true, 2, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'MORNING_EVENING', '주 활동 시간은?', true, 3, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'MORNING_EVENING', '룸메의 외출/복귀 시간이?', true, 4, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
-- 깔끔형/자유형 (CLEAN_DIRTY) - 총 10점
SELECT 'CLEAN_DIRTY', '바닥에 머리카락이 보이면?', true, 1, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'CLEAN_DIRTY', '책상 상태는 보통?', true, 2, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'CLEAN_DIRTY', '청소 주기는?', true, 3, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'CLEAN_DIRTY', '상대방 자리가 더럽다면?', true, 4, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
-- 예민형/둔감형 (SENSITIVE_DULL) - 총 10점
SELECT 'SENSITIVE_DULL', '룸메 전화 통화 소리', true, 1, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'SENSITIVE_DULL', '불 켜진 상태에서 잠들 수 있나', true, 2, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'SENSITIVE_DULL', '키보드/마우스 소리', true, 3, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'SENSITIVE_DULL', '아침 알람 소리', true, 4, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
-- 외향형/내향형 (EXTRO_INTRO) - 총 10점
SELECT 'EXTRO_INTRO', '룸메와의 관계', true, 1, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'EXTRO_INTRO', '룸메가 친구를 데려온다면?', true, 2, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'EXTRO_INTRO', '기숙사에서 보내는 시간은?', true, 3, 3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1)
UNION ALL
SELECT 'EXTRO_INTRO', '룸메가 나의 생활공간을 침범한다면?', true, 4, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM gikbti_question LIMIT 1);

