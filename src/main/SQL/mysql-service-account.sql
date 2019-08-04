CREATE USER 'luckynumbers'@'localhost' IDENTIFIED BY 'kipoderax';

GRANT SELECT ON sjis_bin.* to 'luckynumbers'@'localhost';
GRANT INSERT ON sjis_bin.* to 'luckynumbers'@'localhost';
GRANT DELETE ON sjis_bin.* to 'luckynumbers'@'localhost';
GRANT UPDATE ON sjis_bin.* to 'luckynumbers'@'localhost';