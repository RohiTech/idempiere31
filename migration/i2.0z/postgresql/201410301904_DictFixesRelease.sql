-- Oct 30, 2014 6:57:05 PM COT
UPDATE AD_Column SET AD_Process_ID=NULL,Updated=TO_TIMESTAMP('2014-10-30 18:57:05','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Column_ID=61940
;

-- Oct 30, 2014 8:34:35 PM COT
UPDATE AD_Field SET EntityType='D',Updated=TO_TIMESTAMP('2014-10-30 20:34:35','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Field_ID=202546
;

-- Oct 30, 2014 8:38:17 PM COT
UPDATE AD_TreeNodeMM SET AD_TreeNodeMM_UU='24a76214-340f-47ef-9fed-0e80713075d4' WHERE AD_Tree_ID=10 AND Node_ID=200072
;

-- Oct 30, 2014 8:38:17 PM COT
UPDATE AD_TreeNodeMM SET AD_TreeNodeMM_UU='20f2ad82-f607-4800-8651-d64417559971' WHERE AD_Tree_ID=10 AND Node_ID=200093
;

-- Oct 30, 2014 8:54:47 PM COT
UPDATE AD_Column SET FKConstraintName='SalesRep_CContactActivity', FKConstraintType='N',Updated=TO_TIMESTAMP('2014-10-30 20:54:47','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Column_ID=62467
;

-- Oct 30, 2014 8:54:47 PM COT
ALTER TABLE C_ContactActivity ADD CONSTRAINT SalesRep_CContactActivity FOREIGN KEY (SalesRep_ID) REFERENCES ad_user(ad_user_id) DEFERRABLE INITIALLY DEFERRED
;

-- Oct 30, 2014 8:55:35 PM COT
ALTER TABLE AD_InfoProcess ADD CONSTRAINT ADInfoWindow_ADInfoProcess FOREIGN KEY (AD_InfoWindow_ID) REFERENCES ad_infowindow(ad_infowindow_id) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED
;

-- Oct 30, 2014 8:55:49 PM COT
ALTER TABLE AD_InfoProcess ADD CONSTRAINT ADProcess_ADInfoProcess FOREIGN KEY (AD_Process_ID) REFERENCES ad_process(ad_process_id) DEFERRABLE INITIALLY DEFERRED
;

-- Oct 30, 2014 8:56:10 PM COT
ALTER TABLE AD_PrintFormat_Trl ADD CONSTRAINT adlanguage_adprintformtrl FOREIGN KEY (AD_Language) REFERENCES ad_language(ad_language) DEFERRABLE INITIALLY DEFERRED
;

-- Oct 30, 2014 8:56:26 PM COT
ALTER TABLE AD_PrintFormat_Trl ADD CONSTRAINT adprintformat_trl FOREIGN KEY (AD_PrintFormat_ID) REFERENCES ad_printformat(ad_printformat_id) DEFERRABLE INITIALLY DEFERRED
;

SELECT register_migration_script('201410301904_DictFixesRelease.sql') FROM dual
;

