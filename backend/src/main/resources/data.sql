-- noinspection SqlNoDataSourceInspectionForFile

INSERT INTO iodb.public.event (dtype, eventid, description, status, userid, opinionid)
VALUES ('OpinionReportEvent', 'e8a5b357-db44-4a9a-8118-a35448145e2c', '🐶', 0, '220ab2f4-dc64-4dde-9b4c-a951b4a752a2',
        '863a3c69-dedf-4d82-88fd-457b4a142f3b'),
       ('OpinionReportEvent', '455db3e7-abfd-4d78-9eb4-399625c88959', '😺', 1, '220ab2f4-dc64-4dde-9b4c-a951b4a752a2',
        'db784192-9c14-43af-a895-1ef504e092f0'),
       ('OpinionReportEvent', 'f238c3bd-1b75-43b2-b89e-a47f3fa103cb', 'OpinionReport: TEST', 0, 'd970a20f-6309-4dbc-8656-15c48cec7867',
        'f48bec69-1995-48c0-8ab4-aaee3734f809');
INSERT INTO iodb.public.event (dtype, eventid, description, status, userid, questionid)
VALUES ('QuestionNotifyEvent', '1991023d-8ee5-41fb-b5e3-4076ea49417b', 'New question!!!', 0, 'ff353e00-9772-4616-90d5-b3a310428172',
        '0338ce3f-b8d3-4cf2-b814-b7ddffc17632');
INSERT INTO iodb.public.event (dtype, eventid, description, status, userid, productid)
VALUES ('ProductReportEvent', '1bc29679-fc39-4a1d-b4b7-0f45b45690ed', 'Bad product', 0, '921f2c09-4397-41f8-9397-f6dc09b25e48',
        '7eb2fb7b-9e40-4150-ab16-d1d462e06405');
INSERT INTO iodb.public.event (dtype, eventid, description, status, userid, questionid)
VALUES ('AnswerReportEvent', 'b12ac4f9-9e79-4420-987e-039d3f64bc56', 'Not Good Answer', 0, '388f8d19-668a-4b96-bd8d-74aed2825aba',
        'd60e9ea8-129f-4b8f-b9ab-5cdcd6aee228');
INSERT INTO iodb.public.event (dtype, eventid, description, status, userid, questionid)
VALUES ('QuestionReportEvent', 'd60e9ea8-129f-4b8f-b9ab-5cdcd6aee228', 'Not Good Answer', 0, '388f8d19-668a-4b96-bd8d-74aed2825aba',
        '0338ce3f-b8d3-4cf2-b814-b7ddffc17632');

INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('8378b753-6d05-454b-8447-efb125846fc7', true, 'admin', false, '$2a$10$xjb0b4oZfvGJkIPd2c7FP.owcLUH0h2bIhnTEIUkr4SpofB/cS4yu', 'ADMIN', 'Admin', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('66208864-7b61-4e6e-8573-53863bd93b35', true, 'user', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User', 'LOCAL', false);

INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('12208864-7b61-4e6e-8573-53863bd93b35', true, 'user1', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User1', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('22208864-7b61-4e6e-8573-53863bd93b35', true, 'user2', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User2', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('32208864-7b61-4e6e-8573-53863bd93b35', true, 'user3', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User3', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('42208864-7b61-4e6e-8573-53863bd93b35', true, 'user4', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User4', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('52208864-7b61-4e6e-8573-53863bd93b35', true, 'user5', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User5', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('72208864-7b61-4e6e-8573-53863bd93b35', true, 'user6', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User6', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('82208864-7b61-4e6e-8573-53863bd93b35', true, 'user7', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User7', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('92208864-7b61-4e6e-8573-53863bd93b35', true, 'user8', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User8', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('62108864-7b61-4e6e-8573-53863bd93b35', true, 'user9', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User9', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('62308864-7b61-4e6e-8573-53863bd93b35', true, 'user10', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User10', 'LOCAL', true);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('62408864-7b61-4e6e-8573-53863bd93b35', true, 'user11', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User11', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('62508864-7b61-4e6e-8573-53863bd93b35', true, 'user12', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User12', 'LOCAL', true);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('62608864-7b61-4e6e-8573-53863bd93b35', true, 'user13', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User13', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('62708864-7b61-4e6e-8573-53863bd93b35', true, 'user14', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User14', 'LOCAL', true);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('62808864-7b61-4e6e-8573-53863bd93b35', true, 'user15', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User15', 'LOCAL', false);
INSERT INTO public.app_user (id, active, email, locked, password, role, visible_name, provider, deleted) VALUES ('62908864-7b61-4e6e-8573-53863bd93b35', true, 'user16', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'USER', 'User16', 'LOCAL', true);

INSERT INTO iodb.public.question (question_id, content, date, product_id)
VALUES ('8d2f2aaf-1b98-4d87-b7e3-97bacd955bc6', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sit amet consequat dui. In sit amet libero sed orci malesuada consectetur nec quis nibh. Vestibulum porttitor, nisl et posuere cursus?',
        CURRENT_TIMESTAMP, '4811913c-b953-4856-979b-838488049d07'),
       ('35c3863a-2241-4c05-942b-6df9dabdf193', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sit amet consequat dui. In sit amet libero sed orci malesuada consectetur nec quis nibh. Vestibulum porttitor, nisl et posuere cursus?',
        CURRENT_TIMESTAMP, '4811913c-b953-4856-979b-838488049d07'),
       ('95bc5f23-ac70-4c7e-a01f-4d9e92fad63d', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sit amet consequat dui. In sit amet libero sed orci malesuada consectetur nec quis nibh. Vestibulum porttitor, nisl et posuere cursus?',
        CURRENT_TIMESTAMP, '4811913c-b953-4856-979b-838488049d07');

INSERT INTO iodb.public.answer (answer_id, content, date, question_id)
VALUES ('f04041a1-6314-4afd-91bb-29d016b23b95', 'Yes', CURRENT_TIMESTAMP, '8d2f2aaf-1b98-4d87-b7e3-97bacd955bc6'),
       ('3cafa9ca-f18c-404a-9c1d-d5d1c853eec5', 'No', CURRENT_TIMESTAMP, '35c3863a-2241-4c05-942b-6df9dabdf193');


INSERT INTO public.field (fieldid, name, type) VALUES ('e65b1f71-f3d8-4e16-ba55-df16a316f051', 'Numer', 'Int');
-- INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('1da399ec-7ab6-4621-afab-6eec4f824d29', 'Category_2', null);
-- INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('1da399ec-7ab6-4621-afab-6eec4f824d29', 'e65b1f71-f3d8-4e16-ba55-df16a316f051');
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('b293013b-40da-4dde-a997-bce3068636ec', 'Category_2', null);
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('b293013b-40da-4dde-a997-bce3068636ec', 'e65b1f71-f3d8-4e16-ba55-df16a316f051');

INSERT INTO public.field (fieldid, name, type) VALUES ('8c47ae46-8aa9-11ed-a1eb-0242ac120002', 'testParent', 'testParent');
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('82d58040-8aa9-11ed-a1eb-0242ac120002', 'Category_3', 'b293013b-40da-4dde-a997-bce3068636ec');
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('82d58040-8aa9-11ed-a1eb-0242ac120002', '8c47ae46-8aa9-11ed-a1eb-0242ac120002');

INSERT INTO public.field (fieldid, name, type) VALUES ('f93af257-b5f2-4643-a844-5b2a7d75873f', 'testParent2', 'testParent2');
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('f93af257-b1c2-4643-a844-5b2a7d75873f', 'Category_4', '82d58040-8aa9-11ed-a1eb-0242ac120002');
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('f93af257-b1c2-4643-a844-5b2a7d75873f', 'f93af257-b5f2-4643-a844-5b2a7d75873f');


INSERT INTO iodb.public.product (product_id, category_id, name, description, deleted, confirmed)
VALUES ('4811913c-b953-4856-979b-838488049d07', 'b293013b-40da-4dde-a997-bce3068636ec', 'Product1', 'Description1', false, true),
       ('cbee1685-9b16-47ae-8c34-afa94e4f1a8f', 'b293013b-40da-4dde-a997-bce3068636ec', 'Product2', 'Description2', false, true),
       ('2f335bfb-6805-4a40-ae9c-53b43f22b3ce', 'b293013b-40da-4dde-a997-bce3068636ec', 'Product3', 'Description3', false, true),
       ('b71a95ef-3f19-4ced-a0f4-ebdca10a7668', 'b293013b-40da-4dde-a997-bce3068636ec', 'Product4', 'Description4', false, true),
       ('4950d349-1127-4690-82a0-94fdc81b019b', 'b293013b-40da-4dde-a997-bce3068636ec', 'Product5', 'Description5', false, true),
       ('b4c7b393-2e26-49e2-9783-785583bd4c66', 'b293013b-40da-4dde-a997-bce3068636ec', 'Product6', 'Description6', true, true);

-- INSERT INTO PROPERTIES (PRODUCT_PRODUCT_ID, VALUE, KEY)
-- VALUES ('4811913c-b953-4856-979b-838488049d07', 'value1', 'key1'),
--        ('4811913c-b953-4856-979b-838488049d07', 'value2', 'key2'),
--        ('cbee1685-9b16-47ae-8c34-afa94e4f1a8f', 'value3', 'key3');

INSERT INTO OPINION (OPINION_ID, PRODUCT_ID, DESCRIPTION, RATE, AUTHOR_ID)
VALUES ('6c3a61be-955c-411b-9942-e746cfd0e75b', '4811913c-b953-4856-979b-838488049d07', 'Test desc 1', 2, '12208864-7b61-4e6e-8573-53863bd93b35'),
       ('dc0dac8a-797b-11ed-a1eb-0242ac120002', '4811913c-b953-4856-979b-838488049d07', 'desc 2', 3, '66208864-7b61-4e6e-8573-53863bd93b35');

INSERT INTO ADVANTAGE (ID, VALUE, OPINION_OPINION_ID, OPINION_PRODUCT_ID)
VALUES ('29e8cfb6-7995-11ed-a1eb-0242ac120002', 'Lorem Ipsum', '6c3a61be-955c-411b-9942-e746cfd0e75b', '4811913c-b953-4856-979b-838488049d07'),
       ('37fbe192-7995-11ed-a1eb-0242ac120002' ,'Lorem ipsum dolor sit amet', '6c3a61be-955c-411b-9942-e746cfd0e75b', '4811913c-b953-4856-979b-838488049d07');

INSERT INTO DISADVANTAGE (ID, VALUE, OPINION_OPINION_ID, OPINION_PRODUCT_ID)
VALUES ('43ddbb7a-7995-11ed-a1eb-0242ac120002', 'Lorem Ipsum', '6c3a61be-955c-411b-9942-e746cfd0e75b', '4811913c-b953-4856-979b-838488049d07');


INSERT INTO REACTION (AUTHOR_ID, PRODUCT_ID, OPINION_ID, POSITIVE)
VALUES ('22208864-7b61-4e6e-8573-53863bd93b35', '4811913c-b953-4856-979b-838488049d07', '6c3a61be-955c-411b-9942-e746cfd0e75b', TRUE),
       ('62308864-7b61-4e6e-8573-53863bd93b35', '4811913c-b953-4856-979b-838488049d07', '6c3a61be-955c-411b-9942-e746cfd0e75b', FALSE),
       ('62808864-7b61-4e6e-8573-53863bd93b35', '4811913c-b953-4856-979b-838488049d07', '6c3a61be-955c-411b-9942-e746cfd0e75b', TRUE);
