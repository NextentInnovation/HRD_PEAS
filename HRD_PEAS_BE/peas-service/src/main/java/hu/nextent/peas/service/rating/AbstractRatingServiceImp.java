package hu.nextent.peas.service.rating;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import hu.nextent.peas.exceptions.ExceptionList;
import hu.nextent.peas.jpa.dao.RatingRepository;
import hu.nextent.peas.jpa.entity.Rating;
import hu.nextent.peas.jpa.entity.RatingStatusEnum;
import hu.nextent.peas.jpa.entity.RoleEnum;
import hu.nextent.peas.service.base.BaseDecorator;

public abstract class AbstractRatingServiceImp extends BaseDecorator {

	@Autowired
	protected RatingRepository ratingRepository;
	
	protected Rating checkExists(long ratingId) {
		return ratingRepository.findById(ratingId).orElseThrow(() -> ExceptionList.rating_not_founded(ratingId));
	}
	
	protected void checkCompany(Rating rating) {
    	if (!rating.getCompany().equals(getCurrentCompany())) {
    		throw ExceptionList.rating_invalid_company(rating.getId(), rating.getCompany().getId());
    	}
    }
	
	protected void checkStatus(Rating rating, RatingStatusEnum ... validStatuses) {
    	if (!Arrays.asList(validStatuses).contains(rating.getStatus())) {
    		throw ExceptionList.rating_invalid_status(rating.getId(), Arrays.asList(validStatuses), rating.getStatus());
    	}
    }
	
	protected void checkMyEmployee(Rating rating) {
    	if (!rating.getLeader().equals(getCurrentUser())) {
    		throw ExceptionList.rating_invalid_user(rating.getId(), rating.getLeader().getId());
    	}
    }
    
	protected void checkRight() {
    	if (!getCurrentUserRoleEnum().contains(RoleEnum.LEADER)) {
    		// TODO Meg kell még oldani
    		throw new BadCredentialsException("Not Roles");
    	}
    }
}
