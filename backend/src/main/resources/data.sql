INSERT INTO iodb.public.event (dtype, eventid, description, status, userid, opinionid)
VALUES ('OpinionReportEvent', 'e8a5b357-db44-4a9a-8118-a35448145e2c', 'TEST', 0, '220ab2f4-dc64-4dde-9b4c-a951b4a752a2',
        '863a3c69-dedf-4d82-88fd-457b4a142f3b'),
       ('OpinionReportEvent', '032d9455-952a-404f-8006-873a6b242696', 'TEST', 0, '37509fa8-cf95-4cf3-815e-38f926440caf',
        '628be02c-d73f-4a7d-a596-21080997b436'),
       ('OpinionReportEvent', '82cdb009-cebf-4e2b-8c0d-5deb9b20b8ce', 'TEST', 0, '9a17968a-1bad-4d43-80d7-84440393704d',
        '863a3c69-dedf-4d82-88fd-457b4a142f3b'),
       ('OpinionReportEvent', 'f238c3bd-1b75-43b2-b89e-a47f3fa103cb', 'TEST', 0, 'd970a20f-6309-4dbc-8656-15c48cec7867',
        'f48bec69-1995-48c0-8ab4-aaee3734f809');


INSERT INTO iodb.public.question (question_id, content, date, product_id)
VALUES ('8d2f2aaf-1b98-4d87-b7e3-97bacd955bc6', 'question-content1', CURRENT_DATE, '0e7cc688-ea1c-42c4-8286-f4ee8edcdb25'),
       ('35c3863a-2241-4c05-942b-6df9dabdf193', 'question-content2', CURRENT_DATE, 'b9043342-cd20-4e70-b621-84d395e2a120');

INSERT INTO iodb.public.answer (answer_id, content, date, question_id)
VALUES ('f04041a1-6314-4afd-91bb-29d016b23b95', 'answer-content1', CURRENT_DATE, '8d2f2aaf-1b98-4d87-b7e3-97bacd955bc6'),
       ('3cafa9ca-f18c-404a-9c1d-d5d1c853eec5', 'answer-content2', CURRENT_DATE, '35c3863a-2241-4c05-942b-6df9dabdf193');
