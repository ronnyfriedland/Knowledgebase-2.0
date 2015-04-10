package de.ronnyfriedland.knowledgebase.repository.jcr;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.jackrabbit.ocm.manager.collectionconverter.impl.ManageableCollectionImpl;

/**
 * Custom implementation of {@link ManageableCollectionImpl} to support collection of {@link String}
 *
 * @author ronnyfriedland
 */
public class ManageableStringCollectionImpl extends ManageableCollectionImpl {

    /**
     * Creates a new {@link ManageableStringCollectionImpl} instance.
     * 
     * @param collection the initial collection
     */
    public ManageableStringCollectionImpl(final Collection<String> collection) {
        super(collection);
    }

    /**
     * Creates a new {@link ManageableStringCollectionImpl} instance.
     */
    public ManageableStringCollectionImpl() {
        super(new ArrayList<String>());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.jackrabbit.ocm.manager.collectionconverter.impl.ManageableCollectionImpl#getObjects()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> getObjects() {
        return super.getObjects();
    }
}
