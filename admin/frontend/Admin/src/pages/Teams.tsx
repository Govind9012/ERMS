import { useEffect, useState } from "react";
import { Tree, TreeNode } from "react-organizational-chart";
import { TransformWrapper, TransformComponent } from "react-zoom-pan-pinch";
import "../styles/hierarchy.css";

interface Employee {
  employeeId: string;
  name: string;
  jobTitle: string;
  poc: string;
  location: string;
}

const Node = ({
  name,
  role,
  onClick,
}: {
  name: string;
  role: string;
  onClick: () => void;
}) => (
  <div className="org-node-box" onClick={onClick}>
    <div className="org-avatar" />
    <div className="org-name">{name}</div>
    <div className="org-role">{role}</div>
  </div>
);

export default function HierarchyPage() {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [showManager, setShowManager] = useState(false);
  const [showPOCs, setShowPOCs] = useState(false);
  const [openPOC, setOpenPOC] = useState<string | null>(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/employees")
      .then((res) => res.json())
      .then((data) => setEmployees(data));
  }, []);

  const pocs = Array.from(new Set(employees.map((e) => e.poc)));

  return (
    <div className="orgchart-page">
      <h3>Organization Chart</h3>

      <TransformWrapper
        initialScale={0.85}
        minScale={0.5}
        maxScale={2}
        wheel={{ step: 0.1 }}
        pinch={{ step: 5 }}
        panning={{ velocityDisabled: true }}
        limitToBounds={false}
        
      >
        
        <TransformComponent>
          <div className="orgchart-canvas">
            <Tree
              lineWidth={"1px"}
              lineColor={"#cbd5f5"}
              lineBorderRadius={"6px"}
              label={
                <Node
                  name="Amrish Shah"
                  role="Director"
                  onClick={() => {
                    setShowManager(!showManager);
                    setShowPOCs(false);
                    setOpenPOC(null);
                  }}
                />
              }
            >
              {showManager && (
                <TreeNode
                  label={
                    <Node
                      name="Sagar Shah"
                      role="Manager"
                      onClick={() => {
                        setShowPOCs(!showPOCs);
                        setOpenPOC(null);
                      }}
                    />
                  }
                >
                  {showPOCs &&
                    pocs.map((poc) => (
                      <TreeNode
                        key={poc}
                        label={
                          <Node
                            name={poc}
                            role="POC"
                            onClick={() =>
                              setOpenPOC(openPOC === poc ? null : poc)
                            }
                          />
                        }
                      >
                        {openPOC === poc &&
                          employees
                            .filter((e) => e.poc === poc)
                            .map((emp) => (
                              <TreeNode
                                key={emp.employeeId}
                                label={
                                  <Node
                                    name={emp.name}
                                    role={emp.jobTitle}
                                    onClick={() => {}}
                                  />
                                }
                              />
                            ))}
                      </TreeNode>
                    ))}
                </TreeNode>
              )}
            </Tree>
          </div>
        </TransformComponent>
      </TransformWrapper>
    </div>
  );
}
