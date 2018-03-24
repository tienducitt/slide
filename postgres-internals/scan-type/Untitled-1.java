package jp.co.elecsweb.electi.action.info.all.initGroupEdit;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import jp.co.elecsweb.electi.action.info.all.groupCreate.groupCreateAction;
import jp.co.elecsweb.electi.dao.db.ConnectDb;
import jp.co.elecsweb.electi.dao.db.TInitGroupAuthorityMngDao;
import jp.co.elecsweb.electi.dto.db.TInitGroupAuthorityMngDto;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.util.ArrayList;
import java.util.List;

public class initGroupEditSaveLogic extends initGroupEditAction {
    public boolean dbUpdate(HttpServletRequest request, initGroupEditActionForm form, ServletContext context,
            ActionMessages errors) {
        boolean ret;
        int groupSize = Integer.parseInt(request.getParameter("groupSize"));
        List<TInitGroupAuthorityMngDto> tTInitGroupAuthorityMngDtos = new ArrayList<TInitGroupAuthorityMngDto>();
        List<TInitGroupAuthorityMngDto> tTInitGroupAuthorityMngDtosNew = new ArrayList<TInitGroupAuthorityMngDto>();
        TInitGroupAuthorityMngDto tInitGroupAuthorityMngDto;
        TInitGroupAuthorityMngDto tInitGroupAuthorityMngDtoNew;
        TInitGroupAuthorityMngDao tInitGroupAuthorityMngDao;

        ret = true;
        Connection conn;

        ConnectDb conDb;
        //connect
        conDb = new ConnectDb();
        conn = null;
        try {
            conn = conDb.connectDatabase(context);
            conn.setAutoCommit(false);
            tInitGroupAuthorityMngDao = new TInitGroupAuthorityMngDao();
            // 更新
            for (int i = 0; i < groupSize; i++) {
                int ID = 0;
                ID = Integer.parseInt(request.getParameter("ID" + i));
                //削除
                int deleteFlg = 0;
                deleteFlg = convertChkValue(request.getParameter("deleteflg" + i));
                if (deleteFlg != 0) {
                    if (tInitGroupAuthorityMngDao.isTInitGroupAuthority(ID, conn)) {
                        tInitGroupAuthorityMngDao.delete(ID, conn);
                    }
                    //更新
                } else {
                    tInitGroupAuthorityMngDto = new TInitGroupAuthorityMngDto();
                    tInitGroupAuthorityMngDto.setID(ID);
                    tInitGroupAuthorityMngDto.setPositionCd(request.getParameter("positionCd" + i));
                    tInitGroupAuthorityMngDto.setHeadquartersCd(request.getParameter("headquartersCd" + i));
                    tInitGroupAuthorityMngDto.setSectionCd(request.getParameter("sectionCd" + i));
                    tInitGroupAuthorityMngDto.setBlockCd(request.getParameter("blockCd" + i));
                    tInitGroupAuthorityMngDto.setBranchCd(request.getParameter("branchCd" + i));
                    tInitGroupAuthorityMngDto.setOfficeCd(request.getParameter("officeCd" + i));
                    tInitGroupAuthorityMngDto.setTeamCd(request.getParameter("teamCd" + i));
                    tInitGroupAuthorityMngDto.setAdminAuthorityCd(request.getParameter("adminAuthorityCd" + i));
                    tInitGroupAuthorityMngDto.setCtiGroupCd(convertChkValue(request.getParameter("ctiGroupCd" + i)));
                    tInitGroupAuthorityMngDto
                            .setUserAuthority(convertChkValue(request.getParameter("userAuthority" + i)));
                    tInitGroupAuthorityMngDto
                            .setSystemAuthority(convertChkValue(request.getParameter("systemAuthority" + i)));
                    tInitGroupAuthorityMngDto
                            .setCustomerAuthority(convertChkValue(request.getParameter("customerAuthority" + i)));
                    tInitGroupAuthorityMngDto.setDisableWhisperedAndMonitoredFlg(
                            convertChkValue(request.getParameter("disableWhisperedAndMonitoredFlg" + i)));
                    tInitGroupAuthorityMngDto
                            .setDisableRecFlg(convertChkValue(request.getParameter("disableRecFlg" + i)));
                    tTInitGroupAuthorityMngDtos.add(tInitGroupAuthorityMngDto);
                }

            }
            int insertNum = 0;
            insertNum = groupSize;
            int maxID = tInitGroupAuthorityMngDao.getMaxId(conn) + 1;
            //追加
            while (request.getParameter("positionCd" + insertNum) != null) {
                tInitGroupAuthorityMngDtoNew = new TInitGroupAuthorityMngDto();

                tInitGroupAuthorityMngDtoNew.setID(maxID);
                tInitGroupAuthorityMngDtoNew.setPositionCd(request.getParameter("positionCd" + insertNum));
                tInitGroupAuthorityMngDtoNew.setHeadquartersCd(request.getParameter("headquartersCd" + insertNum));
                tInitGroupAuthorityMngDtoNew.setSectionCd(request.getParameter("sectionCd" + insertNum));
                tInitGroupAuthorityMngDtoNew.setBlockCd(request.getParameter("blockCd" + insertNum));
                tInitGroupAuthorityMngDtoNew.setBranchCd(request.getParameter("branchCd" + insertNum));
                tInitGroupAuthorityMngDtoNew.setOfficeCd(request.getParameter("officeCd" + insertNum));
                tInitGroupAuthorityMngDtoNew.setTeamCd(request.getParameter("teamCd" + insertNum));
                tInitGroupAuthorityMngDtoNew.setAdminAuthorityCd(request.getParameter("adminAuthorityCd" + insertNum));
                tInitGroupAuthorityMngDtoNew
                        .setCtiGroupCd(convertChkValue(request.getParameter("ctiGroupCd" + insertNum)));
                tInitGroupAuthorityMngDtoNew
                        .setUserAuthority(convertChkValue(request.getParameter("userAuthority" + insertNum)));
                tInitGroupAuthorityMngDtoNew
                        .setSystemAuthority(convertChkValue(request.getParameter("systemAuthority" + insertNum)));
                tInitGroupAuthorityMngDtoNew
                        .setCustomerAuthority(convertChkValue(request.getParameter("customerAuthority" + insertNum)));
                tInitGroupAuthorityMngDtoNew.setDisableWhisperedAndMonitoredFlg(
                        convertChkValue(request.getParameter("disableWhisperedAndMonitoredFlg" + insertNum)));
                tInitGroupAuthorityMngDtoNew
                        .setDisableRecFlg(convertChkValue(request.getParameter("disableRecFlg" + insertNum)));
                tTInitGroupAuthorityMngDtosNew.add(tInitGroupAuthorityMngDtoNew);

                maxID++;
                insertNum++;
            }

            for (TInitGroupAuthorityMngDto itemInsert : tTInitGroupAuthorityMngDtosNew) {
                tInitGroupAuthorityMngDao.insert(itemInsert, conn);
            }
            for (TInitGroupAuthorityMngDto item : tTInitGroupAuthorityMngDtos) {
                if (tInitGroupAuthorityMngDao.isTInitGroupAuthority(item.getID(), conn)) {
                    tInitGroupAuthorityMngDao.update(item, conn);
                }
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            ret = false;
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;
        }

        // コミット
        try {
            conn.commit();
            conDb.close(conn);
            conn.close();
        } catch (SQLException e) {
            ret = false;
            e.printStackTrace();
            // 「システムエラーが発生しました。管理者に連絡して下さい。」
            errors.add("error", new ActionMessage("EW001"));
            saveErrors(request, errors);
        }

        return ret;
    }

    public int convertChkValue(String value) {
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value);
    }
}