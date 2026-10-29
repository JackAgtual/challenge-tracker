import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { components } from "@/types/api";
import { GoalDefinitionsTable } from "./GoalDefinitionsTable";
import { CreateGoalForm } from "./CreateGoalForm";

export type Props = {
  challengeId: number;
  goalDefinitions: components["schemas"]["GoalDefinitionResponse"][];
  viewOnly: boolean;
};

export function GoalDefinitionsCard({
  challengeId,
  goalDefinitions,
  viewOnly,
}: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Goal definitions</CardTitle>
        <CardDescription>
          <p>These are the goals that will be completed each day.</p>
        </CardDescription>
      </CardHeader>
      <CardContent>
        {goalDefinitions.length === 0 ? (
          <p>No goal definitions</p>
        ) : (
          <GoalDefinitionsTable
            challengeId={challengeId}
            goalDefinitions={goalDefinitions}
            viewOnly={viewOnly}
          />
        )}
      </CardContent>
      {!viewOnly && (
        <CardFooter>
          <CreateGoalForm challengeId={challengeId} />
        </CardFooter>
      )}
    </Card>
  );
}
