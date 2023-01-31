﻿DROP VIEW rv_inoutdetails;

CREATE OR REPLACE VIEW rv_inoutdetails AS 
SELECT h.ad_client_id,
    h.ad_org_id,
    l.isactive,
    l.created,
    l.createdby,
    l.updated,
    l.updatedby,
    h.m_inout_id,
    h.issotrx,
    h.documentno,
    h.docaction,
    h.docstatus,
    h.posted,
    h.processed,
    h.c_doctype_id,
    h.description,
    h.c_order_id,
    h.dateordered,
    h.movementtype,
    h.movementdate,
    h.dateacct,
    h.c_bpartner_id,
    h.c_bpartner_location_id,
    h.ad_user_id,
    h.salesrep_id,
    h.m_warehouse_id,
    h.poreference,
    h.deliveryrule,
    h.freightcostrule,
    h.freightamt,
    h.deliveryviarule,
    h.m_shipper_id,
    h.priorityrule,
    h.dateprinted,
    h.nopackages,
    h.pickdate,
    h.shipdate,
    h.trackingno,
    h.ad_orgtrx_id,
    h.c_project_id,
    h.c_campaign_id,
    h.c_activity_id,
    h.user1_id,
    h.user2_id,
    h.datereceived,
    h.isapproved,
    h.isindispute,
    l.m_inoutline_id,
    l.line,
    l.description                                 AS linedescription,
    l.c_orderline_id,
    l.m_locator_id,
    l.m_product_id,
    l.c_uom_id,
    l.m_attributesetinstance_id,
    productattribute(l.m_attributesetinstance_id) AS productattribute,
    pasi.m_attributeset_id,
    pasi.m_lot_id,
    pasi.guaranteedate,
    pasi.lot,
    pasi.serno,
    l.movementqty,
    l.qtyentered,
    l.isdescription,
    l.confirmedqty,
    l.pickedqty,
    l.scrappedqty,
    l.targetqty,
    loc.value                                     AS locatorvalue,
    loc.x,
    loc.y,
    loc.z,
    h.c_charge_id                                 AS m_inout_c_charge_id,
    h.chargeamt,
    h.c_invoice_id                                AS m_inout_c_invoice_id,
    h.createconfirm,
    h.created                                     AS m_inout_created,
    h.createdby                                   AS m_inout_createdby,
    h.createfrom,
    h.createpackage,
    h.dropship_bpartner_id,
    h.dropship_location_id,
    h.dropship_user_id,
    h.generateto,
    h.isactive                                    AS m_inout_isactive,
    h.isdropship,
    h.isintransit,
    h.isprinted,
    h.m_rma_id,
    h.processedon,
    h.processing,
    h.ref_inout_id,
    h.reversal_id,
    h.sendemail,
    h.updated                                     AS m_inout_updated,
    h.updatedby                                   AS m_inout_updatedby,
    h.volume,
    h.weight,
    l.ad_org_id                                   AS m_inoutline_ad_org_id,
    l.ad_orgtrx_id                                AS m_inoutline_ad_orgtrx_id,
    l.c_activity_id                               AS m_inoutline_c_activity_id,
    l.c_campaign_id                               AS m_inoutline_c_campaign_id,
    l.c_charge_id                                 AS m_inoutline_c_charge_id,
    l.c_project_id                                AS m_inoutline_c_project_id,
    l.c_projectphase_id,
    l.c_projecttask_id,
    l.isinvoiced,
    l.m_rmaline_id,
    l.processed                                   AS m_inoutline_processed,
    l.ref_inoutline_id,
    l.reversalline_id,
    l.user1_id                                    AS m_inoutline_user1_id,
    l.user2_id                                    AS m_inoutline_user2_id,
    loc.ad_org_id                                 AS m_locator_ad_org_id,
    loc.isactive                                  AS m_locator_isactive,
    loc.isdefault,
    loc.m_warehouse_id                            AS m_locator_m_warehouse_id,
    loc.priorityno,
    pasi.ad_org_id                                AS m_asi_ad_org_id,
    pasi.created                                  AS m_asi_created,
    pasi.createdby                                AS m_asi_createdby,
    pasi.description                              AS m_asi_description,
    pasi.isactive                                 AS m_asi_isactive,
    pasi.updated                                  AS m_asi_updated,
    pasi.updatedby                                AS m_asi_updatedby 
FROM m_inout h 
        JOIN m_inoutline l 
        ON h.m_inout_id = l.m_inout_id 
        LEFT JOIN m_locator loc 
        ON l.m_locator_id = loc.m_locator_id 
        LEFT JOIN m_attributesetinstance pasi 
        ON l.m_attributesetinstance_id = pasi.m_attributesetinstance_id
;
