-- Initial Users Data
-- Note: Password is 'password123' encrypted with BCrypt
-- BCrypt hash generated for 'password123': $2a$10$xY5Z1234567890abcdefghijklmnopqrstuvwxyz
INSERT INTO users (id, username, email, password, bio, roles, created_at, updated_at)
VALUES
    (1, 'alice_parrot', 'alice@parrotsocial.com', '$2a$10$xY5Z1234567890abcdefghijklmnopqrstuvwxyz',
     'Software developer passionate about social media and parrots 🦜', 'ROLE_USER,ROLE_ADMIN',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'bob_developer', 'bob@parrotsocial.com', '$2a$10$xY5Z1234567890abcdefghijklmnopqrstuvwxyz',
     'Full-stack developer | Coffee enthusiast ☕', 'ROLE_USER',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'carol_designer', 'carol@parrotsocial.com', '$2a$10$xY5Z1234567890abcdefghijklmnopqrstuvwxyz',
     'UI/UX Designer creating beautiful experiences', 'ROLE_USER',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Set the sequence for users to continue from id=4
SELECT setval('users_id_seq', 3, true);

-- Initial Posts Data
INSERT INTO posts (id, user_id, content, image_url, likes_count, comments_count, created_at, updated_at)
VALUES
    (1, 1, 'Welcome to Parrot Social! 🦜 Excited to share this new platform with everyone. Let''s connect and share amazing content!',
     'https://images.unsplash.com/photo-1552728089-57bdde30beb3?w=800', 15, 2,
     CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (2, 2, 'Just deployed my first Spring Boot application with Liquibase migrations. The database versioning is so much cleaner now! 🚀',
     NULL, 8, 1,
     CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    (3, 3, 'Working on the new design system for our social platform. Here''s a sneak peek at the color palette 🎨',
     'https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=800', 12, 3,
     CURRENT_TIMESTAMP - INTERVAL '12 hours', CURRENT_TIMESTAMP - INTERVAL '12 hours'),
    (4, 1, 'Fun fact: African Grey Parrots can learn over 100 words and use them in context! Nature is amazing 🦜📚',
     'https://images.unsplash.com/photo-1552728089-57bdde30beb3?w=800', 20, 2,
     CURRENT_TIMESTAMP - INTERVAL '6 hours', CURRENT_TIMESTAMP - INTERVAL '6 hours'),
    (5, 2, 'Coffee + Code = Perfect Monday morning ☕💻',
     NULL, 5, 1,
     CURRENT_TIMESTAMP - INTERVAL '2 hours', CURRENT_TIMESTAMP - INTERVAL '2 hours');

-- Set the sequence for posts to continue from id=6
SELECT setval('posts_id_seq', 5, true);

-- Initial Comments Data
INSERT INTO comments (id, post_id, user_id, content, parent_comment_id, likes_count, created_at, updated_at)
VALUES
    (1, 1, 2, 'This looks amazing! Can''t wait to explore all the features 🎉', NULL, 3,
     CURRENT_TIMESTAMP - INTERVAL '1 day 20 hours', CURRENT_TIMESTAMP - INTERVAL '1 day 20 hours'),
    (2, 1, 3, 'Great work on launching this! The UI looks clean and modern.', NULL, 5,
     CURRENT_TIMESTAMP - INTERVAL '1 day 18 hours', CURRENT_TIMESTAMP - INTERVAL '1 day 18 hours'),
    (3, 2, 1, 'Liquibase is a game changer for database migrations! Good choice 👍', NULL, 2,
     CURRENT_TIMESTAMP - INTERVAL '20 hours', CURRENT_TIMESTAMP - INTERVAL '20 hours'),
    (4, 3, 1, 'Love the color palette! Very modern and accessible.', NULL, 4,
     CURRENT_TIMESTAMP - INTERVAL '10 hours', CURRENT_TIMESTAMP - INTERVAL '10 hours'),
    (5, 3, 2, 'The gradient transitions are beautiful. Can''t wait to see it in the app!', NULL, 3,
     CURRENT_TIMESTAMP - INTERVAL '9 hours', CURRENT_TIMESTAMP - INTERVAL '9 hours'),
    (6, 3, 1, 'Thanks! We focused on WCAG accessibility standards while keeping it vibrant.', 4, 2,
     CURRENT_TIMESTAMP - INTERVAL '8 hours', CURRENT_TIMESTAMP - INTERVAL '8 hours'),
    (7, 4, 3, 'That''s fascinating! I didn''t know parrots were that intelligent 🤯', NULL, 6,
     CURRENT_TIMESTAMP - INTERVAL '5 hours', CURRENT_TIMESTAMP - INTERVAL '5 hours'),
    (8, 4, 2, 'African Greys are incredible. They can even understand concepts like bigger/smaller!', NULL, 4,
     CURRENT_TIMESTAMP - INTERVAL '4 hours', CURRENT_TIMESTAMP - INTERVAL '4 hours'),
    (9, 5, 3, 'The best combination! What are you working on?', NULL, 1,
     CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Set the sequence for comments to continue from id=10
SELECT setval('comments_id_seq', 9, true);

-- Summary of inserted data:
-- Users: 3 users (alice_parrot, bob_developer, carol_designer)
-- Posts: 5 posts with varying content and some with images
-- Comments: 9 comments including nested replies
