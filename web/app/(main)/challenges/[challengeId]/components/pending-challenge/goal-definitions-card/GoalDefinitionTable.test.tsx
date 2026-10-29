import { components } from "@/types/api";
import userEvent, { UserEvent } from "@testing-library/user-event";
import { GoalDefinitionsTable } from "./GoalDefinitionsTable";
import { render, screen, within } from "@testing-library/react";

jest.mock("@/actions/goal-definition", () => ({
  deleteGoal: jest.fn(),
}));

describe("GoalDefinitionTable", () => {
  const challengeId = 1;
  const goals = ["run", "swim", "bike"];
  const goalDefinitions: components["schemas"]["GoalDefinitionResponse"][] =
    goals.map((goal, idx) => ({
      id: idx,
      name: goal,
    }));

  let user: UserEvent;

  beforeEach(() => {
    user = userEvent.setup();
  });

  describe("view only", () => {
    it("does not display Action column", () => {
      render(
        <GoalDefinitionsTable
          challengeId={challengeId}
          goalDefinitions={goalDefinitions}
          viewOnly={true}
        />
      );

      expect(
        screen.queryByRole("columnheader", { name: "Action" })
      ).not.toBeInTheDocument();
    });

    it("renders all goal definitions", async () => {
      render(
        <GoalDefinitionsTable
          challengeId={challengeId}
          goalDefinitions={goalDefinitions}
          viewOnly={true}
        />
      );

      const cells = await screen.findAllByRole("cell");
      expect(cells.map((cell) => cell.textContent)).toEqual(goals);
    });
  });

  describe("not view only", () => {
    it("displays Action column", () => {
      render(
        <GoalDefinitionsTable
          challengeId={challengeId}
          goalDefinitions={goalDefinitions}
          viewOnly={false}
        />
      );

      expect(
        screen.queryByRole("columnheader", { name: "Action" })
      ).toBeInTheDocument();
    });

    it("renders all goal definitions and delete", async () => {
      render(
        <GoalDefinitionsTable
          challengeId={challengeId}
          goalDefinitions={goalDefinitions}
          viewOnly={false}
        />
      );

      //   Don't wnat to keep row header
      const [, ...bodyRows] = await screen.findAllByRole("row");

      bodyRows.forEach((row, i) => {
        expect(
          within(row).getByRole("cell", { name: goals.at(i) })
        ).toBeInTheDocument();
        expect(within(row).getByRole("button")).toBeInTheDocument();
      });
    });
  });
});
