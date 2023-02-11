SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'adapter-dpcr'
  AND pid <> pg_backend_pid(); 
DROP DATABASE IF EXISTS "adapter-dpcr";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'connect-adm'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "connect-adm";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'connect-amm'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "connect-amm";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'connect-dpcr'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "connect-dpcr";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'connect-imm'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "connect-imm";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'connect-omm'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "connect-omm";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'connect-rmm'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "connect-rmm";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'connect-wfm'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "connect-wfm";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'htp-forte-adapter'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "htp-forte-adapter";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'mp96-adapter'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "mp96-adapter";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'mp24-adapter'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "mp24-adapter";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'lp24_adapter'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "lp24-adapter";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'AuditTrailDB'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "AuditTrailDB";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'pas-device'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "pas-device";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'pas-admin'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "pas-admin";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'pas-email'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "pas-email";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'pas-metadata'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "pas_metadata";

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'pas-security'
  AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS "pas-security";