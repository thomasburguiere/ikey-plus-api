package fr.lis.ikeyplus.model;

import fr.lis.ikeyplus.utils.IkeyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Character
 *
 * @author Florian Causse
 */
public class Character implements ICharacter {

    private String name = null;
    private String id = null;
    private float weight = IkeyConfig.DEFAULT_WEIGHT.getIntWeight();
    private ICharacter parentCharacter = null;
    private List<State> inapplicableStates = null;
    private List<ICharacter> childCharacters = null;
    private List<String> mediaObjectKeys = null;

    public Character() {
        this.name = null;
        this.inapplicableStates = new ArrayList<>();
        this.childCharacters = new ArrayList<>();
        this.mediaObjectKeys = new ArrayList<>();
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#isSupportsCategoricalData() */
    @Override
    public boolean isSupportsCategoricalData() {
        return false;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#getId() */
    @Override
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#setId(java.lang.String) */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#getName() */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#setName(java.lang.String) */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#getweight() */
    @Override
    public float getWeight() {
        return weight;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#setWeight(int) */
    @Override
    public void setWeight(float weight) {
        this.weight = weight;

    }

    @Override
    public List<String> getMediaObjectKeys() {
        return mediaObjectKeys;
    }

    public void setMediaObjectKeys(List<String> mediaObjectKey) {
        this.mediaObjectKeys = mediaObjectKey;
    }

    public String getFirstImage(DataSet dataset) {
        if (dataset != null && mediaObjectKeys != null && mediaObjectKeys.size() > 0
                && dataset.getMediaObject(mediaObjectKeys.get(0)).startsWith("http")) {
            return dataset.getMediaObject(mediaObjectKeys.get(0));
        }
        return null;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#getInapplicableStates() */
    @Override
    public List<State> getInapplicableStates() {
        return inapplicableStates;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#setInapplicableStates(java.util.List) */
    public void setInapplicableStates(List<State> inapplicableStates) {
        this.inapplicableStates = inapplicableStates;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#getParentCharacter() */
    @Override
    public ICharacter getParentCharacter() {
        return parentCharacter;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#setParentCharacter(model.ICharacter) */
    @Override
    public void setParentCharacter(ICharacter parentCharacter) {
        this.parentCharacter = parentCharacter;
        this.parentCharacter.getChildCharacters().add(this);
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#getChildCharacters() */
    @Override
    public List<ICharacter> getChildCharacters() {
        return childCharacters;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#setChildCharacters(java.util.List) */
    public void setChildCharacters(List<ICharacter> childCharacters) {
        this.childCharacters = childCharacters;
    }

    /* (non-Javadoc)
     *
     * @see model.ICharacter#getAllChildren() */
    @Override
    public List<ICharacter> getAllChildren() {
        List<ICharacter> allChildrenCharacter = new ArrayList<>();
        addChildrenToList(allChildrenCharacter, this);
        return allChildrenCharacter;
    }

    private void addChildrenToList(List<ICharacter> allChildrenCharacter, ICharacter character) {
        for (ICharacter childCharacter : character.getChildCharacters()) {
            allChildrenCharacter.add(childCharacter);
            addChildrenToList(allChildrenCharacter, childCharacter);
        }
    }

    public String toString() {
        return name;
    }
}
