--USERS
INSERT INTO public.app_user (id, active, deleted, email, locked, password, provider, role, visible_name) VALUES ('8378b753-6d05-454b-8447-efb125846fc7', true, false, 'admin', false, '$2a$10$xjb0b4oZfvGJkIPd2c7FP.owcLUH0h2bIhnTEIUkr4SpofB/cS4yu', 'LOCAL', 'ADMIN', 'Admin');
INSERT INTO public.app_user (id, active, deleted, email, locked, password, provider, role, visible_name) VALUES ('66208864-7b61-4e6e-8573-53863bd93b35', true, false, 'user', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'LOCAL', 'USER', 'Mateusz');
INSERT INTO public.app_user (id, active, deleted, email, locked, password, provider, role, visible_name) VALUES ('12208864-7b61-4e6e-8573-53863bd93b35', true, false, 'user1', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'LOCAL', 'USER', 'Jan');
INSERT INTO public.app_user (id, active, deleted, email, locked, password, provider, role, visible_name) VALUES ('22208864-7b61-4e6e-8573-53863bd93b35', true, false, 'user2', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'LOCAL', 'USER', 'Harry');
INSERT INTO public.app_user (id, active, deleted, email, locked, password, provider, role, visible_name) VALUES ('32208864-7b61-4e6e-8573-53863bd93b35', true, false, 'user3', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'LOCAL', 'USER', 'Kamil');
INSERT INTO public.app_user (id, active, deleted, email, locked, password, provider, role, visible_name) VALUES ('42208864-7b61-4e6e-8573-53863bd93b35', true, false, 'user4', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'LOCAL', 'USER', 'Piotr');
INSERT INTO public.app_user (id, active, deleted, email, locked, password, provider, role, visible_name) VALUES ('52208864-7b61-4e6e-8573-53863bd93b35', true, false, 'user5', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'LOCAL', 'USER', 'Rafał');
INSERT INTO public.app_user (id, active, deleted, email, locked, password, provider, role, visible_name) VALUES ('6ddee1ee-9eba-4222-a031-463a849e1886', true, false, 'Karol2137', false, '$2a$10$76lL2cTtJS1bt3/97Czo0elebu5Lv0oNZytMBBZzqHPgAgyNcSSQG', 'LOCAL', 'USER', 'Karol');

--CATEGORIES
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('831351a7-6d6a-4900-8dd8-edd13c484dee', 'Food', null);
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('3c13f34d-4bc5-448e-a673-032b65333629', 'Cakes', '831351a7-6d6a-4900-8dd8-edd13c484dee');
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('0baae92b-73e8-40a0-bf59-af881ce95e56', 'Electronics', null);
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('683a49da-916f-44b0-b7b7-a327463a435d', 'Entertainment', null);
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('260560a6-93ae-45b9-b3dc-7bdd8a761e0e', 'Games', '683a49da-916f-44b0-b7b7-a327463a435d');
INSERT INTO public.category (categoryid, name, parent_category_category_id) VALUES ('d58ced8d-1622-40d1-9ac5-8a27f66fcc77', 'Books', '683a49da-916f-44b0-b7b7-a327463a435d');

--FIELDS
INSERT INTO public.field (fieldid, name, type) VALUES ('726725f7-1244-4f0a-b064-bdb2c6d177d5', 'Calories', 'Double');
INSERT INTO public.field (fieldid, name, type) VALUES ('e3660f2b-8d66-4a9c-bc09-63a6042b20af', 'Weight (g)', 'Double');
INSERT INTO public.field (fieldid, name, type) VALUES ('4e776f0b-3999-421f-8deb-f4437ab3ccb5', 'Genre', 'String');
INSERT INTO public.field (fieldid, name, type) VALUES ('d79c4d91-4891-4e8a-a660-1ccea1c1da42', 'Developer', 'String');
INSERT INTO public.field (fieldid, name, type) VALUES ('335b1d73-a225-4e45-99ef-484d6d83de09', 'Author', 'String');
INSERT INTO public.field (fieldid, name, type) VALUES ('e8d4ebd0-1528-424d-b1d8-cef651aedbb5', 'Genre', 'String');

--CATEGORY_FIELDS
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('831351a7-6d6a-4900-8dd8-edd13c484dee', '726725f7-1244-4f0a-b064-bdb2c6d177d5');
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('3c13f34d-4bc5-448e-a673-032b65333629', 'e3660f2b-8d66-4a9c-bc09-63a6042b20af');
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('260560a6-93ae-45b9-b3dc-7bdd8a761e0e', '4e776f0b-3999-421f-8deb-f4437ab3ccb5');
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('260560a6-93ae-45b9-b3dc-7bdd8a761e0e', 'd79c4d91-4891-4e8a-a660-1ccea1c1da42');
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('d58ced8d-1622-40d1-9ac5-8a27f66fcc77', '335b1d73-a225-4e45-99ef-484d6d83de09');
INSERT INTO public.category_fields (category_categoryid, fields_fieldid) VALUES ('d58ced8d-1622-40d1-9ac5-8a27f66fcc77', 'e8d4ebd0-1528-424d-b1d8-cef651aedbb5');


--PRODUCTS
INSERT INTO public.product (product_id, confirmed, constant_product_id, created_at, deleted, description, name, category_id) VALUES ('eb62b2ce-d22c-4082-8b39-67e838f4b0d7', true, 'eb62b2ce-d22c-4082-8b39-67e838f4b0d7', '2023-01-08 14:54:07.364890', false, 'Kremówka – w Polsce, ciastko złożone z dwóch płatów ciasta francuskiego, przełożone bitą śmietaną, kremem śmietankowym, budyniowym lub bezowym, zazwyczaj posypane cukrem pudrem.', 'Kremówka', '3c13f34d-4bc5-448e-a673-032b65333629');
INSERT INTO public.product (product_id, confirmed, constant_product_id, created_at, deleted, description, name, category_id) VALUES ('3eb68278-2665-402a-a478-60bdbc5d5904', true, '3eb68278-2665-402a-a478-60bdbc5d5904', '2023-01-08 14:58:35.017023', false, '2015 GOTY', 'The Witcher 3: Wild Hunt', '260560a6-93ae-45b9-b3dc-7bdd8a761e0e');
INSERT INTO public.product (product_id, confirmed, constant_product_id, created_at, deleted, description, name, category_id) VALUES ('5a17c499-673d-4b3d-9046-33d35e600fbf', true, '5a17c499-673d-4b3d-9046-33d35e600fbf', '2023-01-08 15:00:14.077212', false, 'Część sagi Wiedźmin autorstwa Andrzeja Sapkowskiego.', 'Wiedźmin: Krew Ognia', 'd58ced8d-1622-40d1-9ac5-8a27f66fcc77');
INSERT INTO public.product (product_id, confirmed, constant_product_id, created_at, deleted, description, name, category_id) VALUES ('14862741-2260-4aec-870e-1bbf82c127b6', true, '14862741-2260-4aec-870e-1bbf82c127b6', '2023-01-08 15:03:41.398435', false, 'The best game for New Year''s eve.', 'Tomb Raider: Definitive Edition', '260560a6-93ae-45b9-b3dc-7bdd8a761e0e');
INSERT INTO public.product (product_id, confirmed, constant_product_id, created_at, deleted, description, name, category_id) VALUES ('ebcfaf1f-b242-4fce-a2eb-480b9583c114', true, 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '2023-01-08 15:04:22.083789', false, 'Nothing to say here.', 'Cyberpunk 2077', '260560a6-93ae-45b9-b3dc-7bdd8a761e0e');

--PROPERTIES
INSERT INTO public.properties (product_product_id, value, key) VALUES ('eb62b2ce-d22c-4082-8b39-67e838f4b0d7', '120', 'Weight (g)');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('eb62b2ce-d22c-4082-8b39-67e838f4b0d7', '341', 'Calories');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('3eb68278-2665-402a-a478-60bdbc5d5904', 'CD Projekt Red', 'Developer');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('3eb68278-2665-402a-a478-60bdbc5d5904', 'RPG', 'Genre');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('5a17c499-673d-4b3d-9046-33d35e600fbf', 'Andrzej Sapkowski', 'Author');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('5a17c499-673d-4b3d-9046-33d35e600fbf', 'Fantasy', 'Genre');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('14862741-2260-4aec-870e-1bbf82c127b6', 'Square Enix', 'Developer');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('14862741-2260-4aec-870e-1bbf82c127b6', 'Action', 'Genre');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('ebcfaf1f-b242-4fce-a2eb-480b9583c114', 'CD Projekt Red', 'Developer');
INSERT INTO public.properties (product_product_id, value, key) VALUES ('ebcfaf1f-b242-4fce-a2eb-480b9583c114', 'RPG', 'Genre');

--QUESTIONS
INSERT INTO public.question (question_id, content, date, product_id, author_id) VALUES ('48b0048e-ccf0-43f4-b92b-5a6aad736960', 'Czy jest tu ktoś kto nie lubi kremówek?', '2023-01-08 18:11:21.494529', 'eb62b2ce-d22c-4082-8b39-67e838f4b0d7', '6ddee1ee-9eba-4222-a031-463a849e1886');
INSERT INTO public.question (question_id, content, date, product_id, author_id) VALUES ('8936c72e-77f7-4ec0-81d9-d8fcb3870449', 'What are the PC requirements?', '2023-01-08 20:25:50.267196', '14862741-2260-4aec-870e-1bbf82c127b6', '32208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.question (question_id, content, date, product_id, author_id) VALUES ('bdbe2fcf-6203-47d6-8908-ca65b9689396', 'What''s the current price?', '2023-01-08 20:33:54.653048', '14862741-2260-4aec-870e-1bbf82c127b6', '52208864-7b61-4e6e-8573-53863bd93b35');

--ANSWERS
INSERT INTO public.answer (answer_id, content, date, question_id, author_id) VALUES ('a524d75e-927a-4a10-8c46-6321fff6979e', 'Nie wiem.', '2023-01-08 20:26:17.504328', '8936c72e-77f7-4ec0-81d9-d8fcb3870449', '52208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.answer (answer_id, content, date, question_id, author_id) VALUES ('dbc028ea-a233-4280-b953-564a69da1810', 'Core i5 2.66GHz 1GB RAM (4GB RAM Vista/7) GPU 1GB (>GeForce GTX 480) 10GB HDD Windows XP/Vista/7/8', '2023-01-08 20:28:39.574766', '8936c72e-77f7-4ec0-81d9-d8fcb3870449', '42208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.answer (answer_id, content, date, question_id, author_id) VALUES ('9acac245-25b3-492d-a742-4c69bfcb90cf', '80 PLN', '2023-01-08 20:35:11.799086', 'bdbe2fcf-6203-47d6-8908-ca65b9689396', '42208864-7b61-4e6e-8573-53863bd93b35');

--OPINIONS
INSERT INTO public.opinion (opinion_id, product_id, created_at, description, rate, author_id) VALUES ('dba673f8-0526-4cea-941e-3c8ddd5e4f92', 'eb62b2ce-d22c-4082-8b39-67e838f4b0d7', '2023-01-08 18:10:23.229719', 'Przepyszna!', 10, '6ddee1ee-9eba-4222-a031-463a849e1886');
INSERT INTO public.opinion (opinion_id, product_id, created_at, description, rate, author_id) VALUES ('9f3d02be-f106-4081-a732-21b69438773e', '3eb68278-2665-402a-a478-60bdbc5d5904', '2023-01-08 20:24:26.732738', '3 Wiedźmin 3 Najlepszy!', 10, '66208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.opinion (opinion_id, product_id, created_at, description, rate, author_id) VALUES ('7f25a03c-b1f1-46cb-89d4-b1f813ca6dfd', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '2023-01-08 20:24:54.422638', 'Very laggy', 3, '32208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.opinion (opinion_id, product_id, created_at, description, rate, author_id) VALUES ('b0f9495e-13a7-4da1-989c-c403ece4e22d', '14862741-2260-4aec-870e-1bbf82c127b6', '2023-01-08 20:29:48.669658', 'Must have for every New Year''s Eve! Great graphics, better than outside.', 8, '42208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.opinion (opinion_id, product_id, created_at, description, rate, author_id) VALUES ('7009adc1-94c7-4b15-b068-9cf00d325adf', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '2023-01-08 20:31:13.336407', 'After updates it got better.', 7, '42208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.opinion (opinion_id, product_id, created_at, description, rate, author_id) VALUES ('36f81e94-3832-4a66-a15d-ff05f01c0e98', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '2023-01-08 20:32:29.676759', 'Great story!', 10, '52208864-7b61-4e6e-8573-53863bd93b35');

--ADVANTAGE
INSERT INTO public.advantage (id, value, opinion_opinion_id, opinion_product_id) VALUES ('0533a035-22de-45e8-b922-5fa8b103b1a2', 'graphics', 'b0f9495e-13a7-4da1-989c-c403ece4e22d', '14862741-2260-4aec-870e-1bbf82c127b6');
INSERT INTO public.advantage (id, value, opinion_opinion_id, opinion_product_id) VALUES ('b9573aa2-42fa-43cb-baa1-42d06e1bdc8d', 'less lags now', '7009adc1-94c7-4b15-b068-9cf00d325adf', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114');
INSERT INTO public.advantage (id, value, opinion_opinion_id, opinion_product_id) VALUES ('a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9', 'great story', '36f81e94-3832-4a66-a15d-ff05f01c0e98', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114');

--DISADVANTAGE
INSERT INTO public.disadvantage (id, value, opinion_opinion_id, opinion_product_id) VALUES ('561d7a3a-6447-417e-a2ad-2053ec777a8b', 'lags', '7f25a03c-b1f1-46cb-89d4-b1f813ca6dfd', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114');
INSERT INTO public.disadvantage (id, value, opinion_opinion_id, opinion_product_id) VALUES ('b72205fb-27bc-4015-b3ce-1773706d175e', 'a little bit laggy', '36f81e94-3832-4a66-a15d-ff05f01c0e98', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114');

--REACTIONS
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, 'dba673f8-0526-4cea-941e-3c8ddd5e4f92', 'eb62b2ce-d22c-4082-8b39-67e838f4b0d7', '6ddee1ee-9eba-4222-a031-463a849e1886');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, 'dba673f8-0526-4cea-941e-3c8ddd5e4f92', 'eb62b2ce-d22c-4082-8b39-67e838f4b0d7', '66208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, '7f25a03c-b1f1-46cb-89d4-b1f813ca6dfd', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '32208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, '9f3d02be-f106-4081-a732-21b69438773e', '3eb68278-2665-402a-a478-60bdbc5d5904', '32208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, 'dba673f8-0526-4cea-941e-3c8ddd5e4f92', 'eb62b2ce-d22c-4082-8b39-67e838f4b0d7', '42208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, '7009adc1-94c7-4b15-b068-9cf00d325adf', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '42208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (false, '7f25a03c-b1f1-46cb-89d4-b1f813ca6dfd', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '42208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, '36f81e94-3832-4a66-a15d-ff05f01c0e98', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '42208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, '9f3d02be-f106-4081-a732-21b69438773e', '3eb68278-2665-402a-a478-60bdbc5d5904', '42208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (false, 'b0f9495e-13a7-4da1-989c-c403ece4e22d', '14862741-2260-4aec-870e-1bbf82c127b6', '22208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (false, '7f25a03c-b1f1-46cb-89d4-b1f813ca6dfd', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '22208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, '7009adc1-94c7-4b15-b068-9cf00d325adf', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '22208864-7b61-4e6e-8573-53863bd93b35');
INSERT INTO public.reaction (positive, opinion_id, product_id, author_id) VALUES (true, '36f81e94-3832-4a66-a15d-ff05f01c0e98', 'ebcfaf1f-b242-4fce-a2eb-480b9583c114', '22208864-7b61-4e6e-8573-53863bd93b35');

--EVENTS
-- INSERT INTO public.event (dtype, eventid, description, status, answerid, questionid, opinionid, productid, userid) VALUES ('AnswerNotifyEvent', 'd850206c-ce53-40df-b97c-627d2539dfa7', 'New answer to your question: Nie wiem.', 0, null, '8936c72e-77f7-4ec0-81d9-d8fcb3870449', null, null, '32208864-7b61-4e6e-8573-53863bd93b35');
-- INSERT INTO public.event (dtype, eventid, description, status, answerid, questionid, opinionid, productid, userid) VALUES ('AnswerNotifyEvent', '5358a644-2efd-4ab0-b8cf-4fe47e7268c9', 'New answer to your question: Core i5 2.66GHz 1GB RAM (4GB RAM Vista/7) GPU 1GB (>GeForce GTX 480) 10GB HDD Windows XP/Vista/7/8', 0, null, '8936c72e-77f7-4ec0-81d9-d8fcb3870449', null, null, '32208864-7b61-4e6e-8573-53863bd93b35');
-- INSERT INTO public.event (dtype, eventid, description, status, answerid, questionid, opinionid, productid, userid) VALUES ('AnswerNotifyEvent', '36e3c06f-6032-43a4-9c92-f07a3b4641ba', 'New answer to your question: 80 PLN', 0, null, 'bdbe2fcf-6203-47d6-8908-ca65b9689396', null, null, '52208864-7b61-4e6e-8573-53863bd93b35');
