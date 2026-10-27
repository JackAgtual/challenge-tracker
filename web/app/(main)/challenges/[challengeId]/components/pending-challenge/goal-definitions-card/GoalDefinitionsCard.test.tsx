import { render, screen } from "@testing-library/react";
import { GoalDefinitionsCard } from "./GoalDefinitionsCard";

jest.mock("@/actions/goal-definition", () => ({
  deleteGoal: jest.fn(),
}));

describe("GoalDefinitionsCard", () => {
  const challengeId = 1;
  const noGoalDefs = "No goal definitions";

  describe("view only", () => {
    const viewOnly = true;
    it("shows no goal definitions when no goals exist", () => {
      render(
        <GoalDefinitionsCard
          challengeId={challengeId}
          goalDefinitions={[]}
          viewOnly={viewOnly}
        />
      );

      expect(screen.getByText(noGoalDefs)).toBeInTheDocument();
    });

    it("does not render create goal form", () => {
      render(
        <GoalDefinitionsCard
          challengeId={challengeId}
          goalDefinitions={[]}
          viewOnly={viewOnly}
        />
      );

      expect(screen.queryByRole("form")).not.toBeInTheDocument();
    });
  });

  describe("not view only", () => {
    const viewOnly = false;
    it("shows no goal definitions when no goals exist", () => {
      render(
        <GoalDefinitionsCard
          challengeId={challengeId}
          goalDefinitions={[]}
          viewOnly={viewOnly}
        />
      );

      expect(screen.getByText(noGoalDefs)).toBeInTheDocument();
    });

    it("renders create goal form", () => {
      render(
        <GoalDefinitionsCard
          challengeId={challengeId}
          goalDefinitions={[]}
          viewOnly={viewOnly}
        />
      );

      expect(screen.getByRole("form")).toBeInTheDocument();
    });
  });
});
