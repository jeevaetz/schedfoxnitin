/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ProblemsolverTemplate implements Serializable, Comparable<ProblemsolverTemplate> {
    private static final long serialVersionUID = 1L;

    private Integer problemsolverTemplateId;
    private Integer problemSolverType;
    private String problemsolverName;
    private String problemsolverValue;

    public ProblemsolverTemplate() {
    }

    public ProblemsolverTemplate(Record_Set rst) {
        try {
            this.problemsolverTemplateId = rst.getInt("problemsolver_template_id");
        } catch (Exception e) {

        }
        try {
            this.problemSolverType = rst.getInt("problem_solver_type");
        } catch (Exception e) {

        }
        try {
            this.problemsolverName = rst.getString("problemsolver_name");
        } catch (Exception e) {

        }
        try {
            this.problemsolverValue = rst.getString("problemsolver_value");
        } catch (Exception e) {

        }
    }

    public ProblemsolverTemplate(Integer problemsolverTemplateId) {
        this.problemsolverTemplateId = problemsolverTemplateId;
    }

    public ProblemsolverTemplate(Integer problemsolverTemplateId, String problemsolverName) {
        this.problemsolverTemplateId = problemsolverTemplateId;
        this.problemsolverName = problemsolverName;
    }

    public Integer getProblemsolverTemplateId() {
        return problemsolverTemplateId;
    }

    public void setProblemsolverTemplateId(Integer problemsolverTemplateId) {
        this.problemsolverTemplateId = problemsolverTemplateId;
    }

    public String getProblemsolverName() {
        return problemsolverName;
    }

    public void setProblemsolverName(String problemsolverName) {
        this.problemsolverName = problemsolverName;
    }

    public String getProblemsolverValue() {
        return problemsolverValue;
    }

    public void setProblemsolverValue(String problemsolverValue) {
        this.problemsolverValue = problemsolverValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (problemsolverTemplateId != null ? problemsolverTemplateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProblemsolverTemplate)) {
            return false;
        }
        ProblemsolverTemplate other = (ProblemsolverTemplate) object;
        return ( this.problemSolverType == null ? other.problemSolverType == null : this.problemSolverType.equals( other.problemSolverType))
            && (this.problemsolverTemplateId == null ? other.problemsolverTemplateId == null : this.problemsolverTemplateId.equals(other.problemsolverTemplateId))
            && (this.problemsolverName == null ? other.problemsolverName == null : this.problemsolverName.equals(other.problemsolverName))
            && (this.problemsolverValue == null ? other.problemsolverValue == null : this.problemsolverValue.equals(other.problemsolverValue));
      }

    @Override
    public String toString() {
        //return "schedfoxlib.model.ProblemsolverTemplate[problemsolverTemplateId=" + problemsolverTemplateId + "]";
        return this.problemsolverName;
    }

    /**
     * @return the problemSolverType
     */
    public Integer getProblemSolverType() {
        return problemSolverType;
    }

    /**
     * @param problemSolverType the problemSolverType to set
     */
    public void setProblemSolverType(Integer problemSolverType) {
        this.problemSolverType = problemSolverType;
    }

    public int compareTo(ProblemsolverTemplate o)
    {
        return String.CASE_INSENSITIVE_ORDER.compare(this.problemsolverName, o.getProblemsolverName());
    }
}
