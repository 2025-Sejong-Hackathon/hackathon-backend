-- GIKBTI 옵션 초기 데이터
-- 각 질문(question_id)에 대해 2개의 옵션이 있음
-- is_true: true는 앞의 타입(M, C, S, E), false는 뒤의 타입(E, D, D, I)
-- 중복 방지: 옵션이 없을 때만 삽입

-- 아침형/저녁형 옵션
INSERT INTO gikbti_option (gikbti_question_id, text, is_true, display_order, created_at, updated_at)
SELECT * FROM (VALUES
    (1, '12시 이전에 자요', true, 1, NOW(), NOW()),
    (1, '새벽 2-3시에 자요', false, 2, NOW(), NOW()),
    (2, '8시 쯤 일어나서 하루를 시작해요', true, 1, NOW(), NOW()),
    (2, '나에게 오전은 없다', false, 2, NOW(), NOW()),
    (3, '낮시간에 일어나서 작업을 해요', true, 1, NOW(), NOW()),
    (3, '밤에 작업을 많이 해요', false, 2, NOW(), NOW()),
    (4, '이르거나 늦어도 괜찮다', true, 1, NOW(), NOW()),
    (4, '잘 때 그러면 힘들다', false, 2, NOW(), NOW())
) AS t(question_id, text, is_true, display_order, created_at, updated_at)
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option WHERE gikbti_question_id = t.question_id);

-- 깔끔형/자유형 옵션
INSERT INTO gikbti_option (gikbti_question_id, text, is_true, display_order, created_at, updated_at)
SELECT * FROM (VALUES
    (5, '바로 치운다', true, 1, NOW(), NOW()),
    (5, '한번에 몰아서 치운다', false, 2, NOW(), NOW()),
    (6, '항상 정리된 편', true, 1, NOW(), NOW()),
    (6, '더러운편이다', false, 2, NOW(), NOW()),
    (7, '1일 / 3일', true, 1, NOW(), NOW()),
    (7, '1주 / 1개월', false, 2, NOW(), NOW()),
    (8, '참을 수 없다', true, 1, NOW(), NOW()),
    (8, '상관없다', false, 2, NOW(), NOW())
) AS t(question_id, text, is_true, display_order, created_at, updated_at)
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option WHERE gikbti_question_id = t.question_id);

-- 예민형/둔감형 옵션
INSERT INTO gikbti_option (gikbti_question_id, text, is_true, display_order, created_at, updated_at)
SELECT * FROM (VALUES
    (9, '나가서 했으면 좋겠다', true, 1, NOW(), NOW()),
    (9, '상관없다', false, 2, NOW(), NOW()),
    (10, '힘들다', true, 1, NOW(), NOW()),
    (10, '가능하다', false, 2, NOW(), NOW()),
    (11, '거슬린다', true, 1, NOW(), NOW()),
    (11, '신경 안쓴다', false, 2, NOW(), NOW()),
    (12, '바로 끈다', true, 1, NOW(), NOW()),
    (12, '잘 못들어서 여러번 울린다', false, 2, NOW(), NOW())
) AS t(question_id, text, is_true, display_order, created_at, updated_at)
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option WHERE gikbti_question_id = t.question_id);

-- 외향형/내향형 옵션
INSERT INTO gikbti_option (gikbti_question_id, text, is_true, display_order, created_at, updated_at)
SELECT * FROM (VALUES
    (13, '밥도 먹고 친해지고 싶다', true, 1, NOW(), NOW()),
    (13, '거리를 두고 싶다', false, 2, NOW(), NOW()),
    (14, '미리 말하면 괜찮다', true, 1, NOW(), NOW()),
    (14, '싫다', false, 2, NOW(), NOW()),
    (15, '잘때만 들어간다', true, 1, NOW(), NOW()),
    (15, '수업시간 빼고 많이 들어간다', false, 2, NOW(), NOW()),
    (16, '절대 안됨!! 내 공간 지켜', true, 1, NOW(), NOW()),
    (16, '뭐 어때', false, 2, NOW(), NOW())
) AS t(question_id, text, is_true, display_order, created_at, updated_at)
WHERE NOT EXISTS (SELECT 1 FROM gikbti_option WHERE gikbti_question_id = t.question_id);

