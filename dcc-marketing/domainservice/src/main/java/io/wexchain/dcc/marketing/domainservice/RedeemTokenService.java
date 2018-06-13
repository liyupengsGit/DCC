package io.wexchain.dcc.marketing.domainservice;

import io.wexchain.dcc.marketing.api.constant.RedeemTokenQualification;
import io.wexchain.dcc.marketing.api.model.request.*;
import io.wexchain.dcc.marketing.domain.IdRestriction;
import io.wexchain.dcc.marketing.domain.RedeemToken;

import java.util.List;

public interface RedeemTokenService {

    RedeemTokenQualification getRedeemTokenQualification(
            GetRedeemTokenQualificationRequest request);

    RedeemToken redeemToken(RedeemTokenRequest request);

    List<RedeemToken> queryRedeemToken(QueryRedeemTokenRequest request);

    List<IdRestriction> queryIdRestriction(QueryIdRestrictionRequest request);

    RedeemToken createBonus(RedeemTokenRequest request);

    RedeemToken applyBonus(ApplyBonusRequest request);
}
