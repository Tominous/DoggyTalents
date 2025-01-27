package doggytalents.entity.features;

import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import net.minecraft.util.text.TranslationTextComponent;

public class DogGenderFeature extends DogFeature {
	
	public DogGenderFeature(EntityDog dogIn) {
		super(dogIn);
	}
	
	public TranslationTextComponent getGenderSubjectFromString(String gender) {
		if(Constants.DOG_GENDER && gender == "male") {
			return new TranslationTextComponent("dog.gender.male.subject");
		}else if (Constants.DOG_GENDER && gender == "female"){
			return new TranslationTextComponent("dog.gender.female.subject");
		}else{
			return new TranslationTextComponent("dog.gender.neuter.subject");
		}	
	}
	
	public boolean checkGender(EntityDog matedog) {
		if(Constants.DOG_GENDER && dog.getGender() == matedog.getGender())
			return false;
		else if(Constants.DOG_GENDER && dog.getGender() != matedog.getGender() && (matedog.getGender().isEmpty() || dog.getGender().isEmpty()))
			return false;
		else if(Constants.DOG_GENDER && dog.getGender() != matedog.getGender())
			return true;
		else
			return true;
	}
	
	public TranslationTextComponent getGenderPronoun() {
		if (Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("male")) {
			return new TranslationTextComponent("dog.gender.male.pronoun");
		}
		else if(Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("female")){
			return new TranslationTextComponent("dog.gender.female.pronoun");
		}else {
			return new TranslationTextComponent("dog.gender.neuter.pronoun");
		}
	}

	public TranslationTextComponent getGenderSubject() {
		if (Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("male")) {
			return new TranslationTextComponent("dog.gender.male.subject");
		}
		else if(Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("female")){
			return new TranslationTextComponent("dog.gender.female.subject");
		}else{
			return new TranslationTextComponent("dog.gender.neuter.subject");
		}
	}
	
	public TranslationTextComponent getGenderTitle() {
		if (Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("male")) {
			return new TranslationTextComponent("dog.gender.male.title");
		}
		else if(Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("female")){
			return new TranslationTextComponent("dog.gender.female.title");
		}else {
			return new TranslationTextComponent("dog.gender.neuter.title");
		}
	}
	
	public String getGenderTip() {
		if(Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("male")) {
			return "dog.gender.male.indicator";
		}else if(Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("female")) {
			return "dog.gender.female.indicator";
		}else{
			return "";
		}
	}
	
	public String getGenderName() {
		if(Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("male")) {
			return "dog.gender.male";
		}else if(Constants.DOG_GENDER && dog.getGender().equalsIgnoreCase("female")) {
			return "dog.gender.female";
		}else{
			return "";
		}
	}
}
