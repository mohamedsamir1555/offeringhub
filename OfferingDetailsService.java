
package com.etisalat.dynamicOffering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.etisalat.dynamicOffering.controller.api.request.OfferChannelDTO;
import com.etisalat.dynamicOffering.controller.api.request.OfferDefinitionDTO;
import com.etisalat.dynamicOffering.controller.api.request.OfferPxDynOfferingParametersDTO;
import com.etisalat.dynamicOffering.controller.api.request.PxOfferDTO;
import com.etisalat.dynamicOffering.database.ods.entity.DynOfferingConverter;
import com.etisalat.dynamicOffering.database.ods.entity.DynOfferingParameterId;
import com.etisalat.dynamicOffering.database.ods.entity.OfferingBonus;
import com.etisalat.dynamicOffering.database.ods.entity.OfferingCapping;
import com.etisalat.dynamicOffering.database.ods.entity.OfferingChannel;
import com.etisalat.dynamicOffering.database.ods.entity.OfferingContradiction;
import com.etisalat.dynamicOffering.database.ods.entity.OfferingDetailsAtl;
import com.etisalat.dynamicOffering.database.ods.entity.OfferingDetailsBtl;
import com.etisalat.dynamicOffering.database.ods.entity.OfferingRateplan;
import com.etisalat.dynamicOffering.database.ods.entity.OfferingThreshold;
import com.etisalat.dynamicOffering.database.ods.entity.PxDynOfferingParameters;
import com.etisalat.dynamicOffering.database.ods.entity.RunDetails;
import com.etisalat.dynamicOffering.database.ods.repository.DynOfferingParameterRepositoryTOds;
import com.etisalat.dynamicOffering.database.ods.repository.OfferingContradictionRepositoryOds;
import com.etisalat.dynamicOffering.database.ods.repository.OfferingDetailsAtlRepositoryOds;
import com.etisalat.dynamicOffering.database.ods.repository.OfferingDetailsBtlRepositoryOds;
import com.etisalat.dynamicOffering.database.trm.entity.Offering;
import com.etisalat.dynamicOffering.database.trm.entity.OfferingDetails;
import com.etisalat.dynamicOffering.mapper.DynamicOfferingMapper;
import com.etisalat.rtim.integration.RTIMintegration;
import com.google.gson.Gson;

@Service
public class OfferingDetailsService extends AbstractBaseService {

	Sort sort;

	@Autowired
	com.etisalat.dynamicOffering.database.trm.repository.OfferingRepositoryTrm OfferingRepositoryTrm;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.DynOfferingConverterRepositoryOds DynOfferingConverterRepositoryOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.DynOfferingParameterLkpRepositoryOds DynOfferingParameterLkpRepositoryOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.DynOfferingParameterRepositoryOds DynOfferingParameterRepositoryOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.OfferingBonusRepositoryTOds OfferingBonusRepositoryTOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.OfferingCappingRepositoryOds OfferingCappingRepositoryOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.OfferingChannelRepositoryOds OfferingChannelRepositoryOds;
	@Autowired
	OfferingContradictionRepositoryOds OfferingContradictionRepositoryOds;

	@Autowired
	OfferingDetailsAtlRepositoryOds atlRepositoryOds;

	@Autowired
	OfferingDetailsBtlRepositoryOds btlRepositoryOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.OfferingRateplanRepositoryOds OfferingRateplanRepositoryOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.OfferingThresholdRepositoryOds OfferingThresholdRepositoryOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.RunDetailsRepositoryOds RunDetailsRepositoryOds;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.OfferingRateplanTRepositoryOds OfferingRateplanTRepositoryOds;

	@Autowired
	com.etisalat.dynamicOffering.database.trm.repository.OfferingDetailsRepositoryTrm offeringDetailsRepositoryTrm;
	@Autowired
	com.etisalat.dynamicOffering.database.ods.repository.RunDetailsRepositoryTOds RunDetailsRepositoryTOds;
	@Autowired
	RTIMintegration rTIMintegration = new RTIMintegration();
	@Autowired
	DynOfferingParameterRepositoryTOds dynamicofferingparameterrepositryods;

	@Autowired
	Gson gson;

	//

	@Transactional()
	public List<OfferingDetailsAtl> findAllATLs() {
		return atlRepositoryOds.findAll();
	}

	@Transactional()
	public List<OfferingDetailsBtl> findAllBTLs() {
		return btlRepositoryOds.findAll();
	}

	@Transactional()
	public List<OfferingDetails> findAllTRMOfferingDetails() {
		return offeringDetailsRepositoryTrm.findAll();
	}

	@Transactional()
	public List<OfferingDetails> findTRMOfferingDetailsByCategoryId(String categoryId) {
		return offeringDetailsRepositoryTrm.findByOfferingCategory(categoryId);
	}

	@Transactional()
	public List<OfferingDetails> findTRMOfferingDetailsByOfferName(String offerName) {
		return offeringDetailsRepositoryTrm.findByOfferingName(offerName);
	}

	@Transactional()
	public OfferingDetailsAtl insertATL(OfferingDetailsAtl atlOffering) {
		return atlRepositoryOds.save(atlOffering);
	}

	@Transactional()
	public OfferingDetailsBtl insertBTL(OfferingDetailsBtl btlOffering) {
		return btlRepositoryOds.save(btlOffering);
	}

	@Transactional()
	public OfferingDetails insertOfferingDetailsTRM(OfferingDetails offeringDetails) {
		return offeringDetailsRepositoryTrm.save(offeringDetails);
	}

	@Transactional()
	public OfferingDetails insertOfferingDetails(OfferingDetails offeringDetails) throws Exception {
		if (offeringDetails.getOfferingLineType().equals("ATL")) {
			OfferingDetailsAtl offeringDetailsAtl = DynamicOfferingMapper.instance
					.mapOfferingDetailsToAtlEntity(offeringDetails);
			insertATL(offeringDetailsAtl);
		} else if (offeringDetails.getOfferingLineType().equals("BTL")) {
			OfferingDetailsBtl offeringDetailsBtl = DynamicOfferingMapper.instance
					.mapOfferingDetailsToBtlEntity(offeringDetails);
			insertBTL(offeringDetailsBtl);
		}
		OfferingDetails offerDetails = offeringDetailsRepositoryTrm.save(offeringDetails);
		rTIMintegration.insertRTIMDB(gson.toJson(offeringDetails),
				offeringDetails.getOfferingLineType().equalsIgnoreCase("ATL") ? "px_offering_details_atl"
						: "px_offering_details_btl");
		return offerDetails;
	}

	/** msamir **/

	Page<OfferingDetails> getrtmoffers(Pageable pageable) {
		return offeringDetailsRepositoryTrm.findAll(pageable);

	}

	@Transactional()
	public void delete(Integer offeringId) {

		if (offeringId != null) {

			DynOfferingConverterRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering converter table " + offeringId);

			DynOfferingParameterLkpRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering parameter table " + offeringId);

			OfferingBonusRepositoryTOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering bonus table " + offeringId);

			OfferingCappingRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering capping table " + offeringId);

			OfferingChannelRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering channel table " + offeringId);

			OfferingContradictionRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering contradicting table " + offeringId);

			atlRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering parameter Ddb " + offeringId);

			btlRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering parameter Ddb " + offeringId);

			OfferingRateplanTRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering parameter Ddb " + offeringId);

			OfferingThresholdRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in offering parameter Ddb " + offeringId);

			RunDetailsRepositoryTOds.delete(offeringId);
			LOGGER.debug("Delete offering id in Rtm DB " + offeringId);

			atlRepositoryOds.delete(offeringId);
			LOGGER.debug("Delete offering id in ATLOds DB " + offeringId);

			btlRepositoryOds.delete(offeringId);

			LOGGER.debug("Delete offering id in BTLOds DB " + offeringId);

			OfferingRepositoryTrm.update(offeringId);
			LOGGER.debug("update offering id in rtm set delete flag =Y " + offeringId);

		}
	}

	public List<Offering> getofferslistrtm() {
		return OfferingRepositoryTrm.getofferlist();
	}

	public List<OfferingDetails> findTRMOfferingDetailsByOfferId(Integer offeringId) {
		return offeringDetailsRepositoryTrm.findByOfferingId(offeringId);
	}

	public Page<OfferingDetails> findTRMOfferingDetailsByOfferVal(Integer offeringVal, Pageable pageable) {

		List<OfferingDetails> offerdetalis = offeringDetailsRepositoryTrm.findByOfferingVal(offeringVal, pageable);

		int ListSize = 0;

		if (offerdetalis != null) {
			ListSize = counter(offeringVal);
		}

		return new PageImpl<OfferingDetails>(offerdetalis, pageable, ListSize);

	}

	public int counter(Integer offeringVal) {
		return offeringDetailsRepositoryTrm.countOfferingval(offeringVal);
	}

	//////////////

	public PxOfferDTO getAllinformationforofferingByofferingid(Integer offeringId) {

		PxOfferDTO pexofferdto = new PxOfferDTO();

		// List<DynOfferingConverter> getofferingconverterByofferingid(Integer
		// offeringId) {

		List<DynOfferingConverter> dynamicofferconverter = (List<DynOfferingConverter>) DynOfferingConverterRepositoryOds
				.findOne(offeringId);

		pexofferdto.getConfiguration().getOfferingConverter()
				.setConverterType(dynamicofferconverter.get(0).getConverterType().intValue());
		pexofferdto.getConfiguration().getOfferingConverter().setFees(dynamicofferconverter.get(0).getFees());
		pexofferdto.getConfiguration().getOfferingConverter()
				.setPaymentMethod(dynamicofferconverter.get(0).getPaymentMethod());
		pexofferdto.getConfiguration().getOfferingConverter().setValidity(dynamicofferconverter.get(0).getValidity());

		List<PxDynOfferingParameters> offeringparameter = (List<PxDynOfferingParameters>) dynamicofferingparameterrepositryods
				.findOne(offeringId);

		List<OfferingBonus> offeringbonus = (List<OfferingBonus>) OfferingBonusRepositoryTOds.findOne(offeringId);

		pexofferdto.getConfiguration().getBonus().setBonusCapping(offeringbonus.get(0).getBonusCapping());
		pexofferdto.getConfiguration().getBonus().setBonusValue(offeringbonus.get(0).getBonusValue());
		pexofferdto.getConfiguration().getBonus().setThresholdId(offeringbonus.get(0).getThresholdId());

		List<OfferingCapping> offeringcapping = (List<OfferingCapping>) OfferingCappingRepositoryOds
				.findOne(offeringId);

		pexofferdto.getConfiguration().getPxOfferingCapping()
				.setCappingDaily(offeringcapping.get(0).getCappingDaily().intValue());
		pexofferdto.getConfiguration().getPxOfferingCapping()
				.setCappingWeekly(offeringcapping.get(0).getCappingWeekly().intValue());
		pexofferdto.getConfiguration().getPxOfferingCapping()
				.setCappingMonthly(offeringcapping.get(0).getCappingMonthly().intValue());
		pexofferdto.getConfiguration().getPxOfferingCapping()
				.setCappingTotal(offeringcapping.get(0).getCappingTotal().intValue());
		pexofferdto.getConfiguration().getPxOfferingCapping().setTargetBundle(offeringcapping.get(0).getTargetBundle());
		pexofferdto.getConfiguration().getPxOfferingCapping()
				.setTargetRatePlan(offeringcapping.get(0).getTargetRatePlan());
		pexofferdto.getConfiguration().getPxOfferingCapping()
				.setTransactionAmount(offeringcapping.get(0).getTransactionAmount().intValue());
		pexofferdto.getConfiguration().getPxOfferingCapping()
				.setProductValidity(offeringcapping.get(0).getProductValidity());

		List<OfferingChannel> offeringchannel = (List<OfferingChannel>) OfferingChannelRepositoryOds
				.findOne(offeringId);

		List<OfferChannelDTO> offerchannellist = new ArrayList<OfferChannelDTO>();
		for (OfferingChannel e : offeringchannel) {
			OfferChannelDTO offerchaneldto = new OfferChannelDTO();
			offerchaneldto.setChannelId(e.getOfferingId());
			offerchaneldto.setChannelId(e.getChannelId().intValue());
			offerchaneldto.setOfferingId(e.getOfferingId());
			offerchannellist.add(offerchaneldto);

		}

		pexofferdto.getDefinition().setOfferingChannelList(offerchannellist);

		// OfferDefinitionDTO d=new OfferDefinitionDTO();
		// d.setOfferingChannelList(olist);

		// pexofferdto.setDefinition(d);

		List<OfferingContradiction> offercontradictiong = (List<OfferingContradiction>) OfferingContradictionRepositoryOds
				.findOne(offeringId);

		// mafesh return type fel pexoffer

		List<OfferingDetailsAtl> offeringdeatalis = (List<OfferingDetailsAtl>) atlRepositoryOds.findOne(offeringId);

		pexofferdto.getDefinition().setOfferName(offeringdeatalis.get(0).getOfferingName());
		pexofferdto.getDefinition().setFees(offeringdeatalis.get(0).getOfferOptinFees());
		pexofferdto.getDefinition().setOfferingChannelList(offerchannellist);
		pexofferdto.getDefinition().getPxShortCode()
				.setShortCode(Integer.parseInt(offeringdeatalis.get(0).getShortCodeNum()));
		
		pexofferdto.getDefinition().getPxShortCode().setServiceId(offeringdeatalis.get(0).getSssId());
		pexofferdto.getDefinition().getPxShortCode().setServiceName(offeringdeatalis.get(0).getSssName());
		pexofferdto.getDefinition().getPxShortCode().setTemplateId(offeringdeatalis.get(0).getPromotionPlanId());
		
		pexofferdto.getDefinition().setRatePlan(offeringdeatalis.get(0).getRatePlanFlag());
		pexofferdto.getDefinition().setOnline(offeringdeatalis.get(0).getOnlineFlag()>0);//
		pexofferdto.getDefinition().setOptinFeesFlag(offeringdeatalis.get(0).getOfferOptinFees()>0);
		//pexofferdto.getDefinition().setCategories(categories);//
		//pexofferdto.getDefinition().setSelectedCategory(selectedCategory);//
		pexofferdto.getDefinition().setSelectedCampaignType(offeringdeatalis.get(0).getOfferingLineType());//linetype
		pexofferdto.getDefinition().getPxServiceCategory().setCategoryId(offeringdeatalis.get(0).getOfferingCategory());
		//pexofferdto.getDefinition().getPxServiceCategory().setParentID(parentID);
		pexofferdto.getDefinition().getPxServiceCategory().setName(offeringdeatalis.get(0).getSssName());
		//pexofferdto.getDefinition().getPxServiceCategory().setArabicName(offeringdeatalis.get(0).geta);//
		//pexofferdto.getDefinition().getPxServiceCategory().setCatHiddenFlag(catHiddenFlag);//
		//pexofferdto.getDefinition().getPxServiceCategory().setCategoryPriority(categoryPriority);
		//pexofferdto.getDefinition().getPxServiceCategory().setDwhEntryDate(offeringdeatalis.get(0).getd);
		pexofferdto.getDefinition().setUssdShortDescription(offeringdeatalis.get(0).getOfferingShortDesc());
		pexofferdto.getDefinition().setUssdLongDescription(offeringdeatalis.get(0).getOfferingLongDesc());
		pexofferdto.getDefinition().setMediaArabicDescription(offeringdeatalis.get(0).getOfferingArDesc());
		pexofferdto.getDefinition().setMediaEnglishDescription(offeringdeatalis.get(0).getOfferingArDesc());
		pexofferdto.getDefinition().setOfferingChannelList(offerchannellist);
		pexofferdto.getDefinition().setPullOffer(offeringdeatalis.get(0).getRedemptionWindow()>0);
		pexofferdto.getDefinition().setInformativeOffer(offeringdeatalis.get(0).getIsInformative()>0);
		pexofferdto.getDefinition().setCommercialEnglishServiceDescription(offeringdeatalis.get(0).getCommercialServiceEndesc());
		pexofferdto.getDefinition().setCommercialArabicServiceDescription(offeringdeatalis.get(0).getCommercialServiceArdesc());
		pexofferdto.getDefinition().setOptinScriptAr(offeringdeatalis.get(0).getOptinScriptAr());
		pexofferdto.getDefinition().setOptinScriptEn(offeringdeatalis.get(0).getOptinScriptEn());
		pexofferdto.getDefinition().setRatePlan(offeringdeatalis.get(0).getRatePlanFlag());
		pexofferdto.getDefinition().getPxOfferingTemplates().setTemplateId(offeringparameter.get(0).getParameter1());
		pexofferdto.getDefinition().getPxOfferingTemplates().setTemplateName(offeringparameter.get(0).getParameterTxt());
		//pexofferdto.getDefinition().setOTHER(oTHER);
		//pexofferdto.getDefinition().setPlatformEnglishDescription(platformEnglishDescription);
		//pexofferdto.getDefinition().setPlatformArabicDescription(platformArabicDescription);//
		pexofferdto.getDefinition().setPlatformId(offeringdeatalis.get(0).getPlatformId());
		pexofferdto.getDefinition().setProductName(offeringdeatalis.get(0).getProductName());
		pexofferdto.getDefinition().setOperationId(offeringdeatalis.get(0).getOperationId());
		pexofferdto.getDefinition().setBundleType(offeringcapping.get(0).getBundleType().toString());
		pexofferdto.getDefinition().getPxOfferingOffval().setOfferingVal(offeringdeatalis.get(0).getOfferingVal());
		pexofferdto.getDefinition().getPxOfferingOffval().setOfferType(offeringparameter.get(0).getParameterTxt());
		pexofferdto.getDefinition().getPxOfferingOffval().setParameterName(parameterName);
		pexofferdto.getDefinition().getPxOfferingOffval().setParameterValue(parameterValue);
		pexofferdto.getDefinition().getPxOfferingOffval().setServiceId(offeringdeatalis.get(0).getSssId());
		
		
		
		List<OfferingRateplan> offeringrateplan = (List<OfferingRateplan>) OfferingRateplanTRepositoryOds
				.findOne(offeringId);

		pexofferdto.getDefinition().setRatePlan(offeringrateplan.get(0).getRatePlanProductId());

		List<OfferingThreshold> offeringthreshold = (List<OfferingThreshold>) OfferingThresholdRepositoryOds
				.findOne(offeringId);

		// mafesh return type fel pexofferdto

		List<RunDetails> rundetalisoffer = (List<RunDetails>) RunDetailsRepositoryTOds.findOne(offeringId);

		// mafesh return type fel pexofferdto or any object

		return pexofferdto;

	}

}
