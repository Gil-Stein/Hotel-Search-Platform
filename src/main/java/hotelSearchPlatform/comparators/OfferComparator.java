package hotelSearchPlatform.comparators;

import java.util.Comparator;

import hotelSearchPlatform.entities.Offer;

public class OfferComparator implements Comparator<Offer>{

	@Override
	public int compare(Offer o1, Offer o2) {
		int priceCmp =  Integer.valueOf(o1.getPrice()).compareTo(Integer.valueOf(o2.getPrice()));
		if (priceCmp !=0)
			return priceCmp;
		int cpcCmp = Integer.valueOf(o1.getCpc()).compareTo(Integer.valueOf(o2.getCpc()));
		if (cpcCmp !=0)
			return -cpcCmp;
		int idCmp = Integer.valueOf(o1.getAdvertiser_id()).compareTo(Integer.valueOf(o2.getAdvertiser_id()));
		if (idCmp !=0)
			return idCmp;
		return 0;
	}

}
